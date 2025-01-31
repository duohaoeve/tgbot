package tg.bot.tw.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.i2p.crypto.eddsa.Utils;
import org.p2p.solanaj.core.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.tw.entity.DepositRecord;
import tg.bot.tw.entity.MyWallet;
import tg.bot.tw.entity.SysUser;
import tg.bot.tw.enums.ActionEnum;
import tg.bot.tw.enums.ActionZnEnum;
import tg.bot.tw.service.*;
import tg.bot.tw.utils.CryptoUtil;
import tg.bot.tw.utils.DateUtils;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class ActionServiceImpl implements ActionService {

    @Value("${crypto.sol.jmKey}")
    private String jmKey;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MyWalletService walletService;
    @Autowired
    private SolanaService solanaService;
    @Autowired
    private TwService twService;
    @Autowired
    private DepositRecordService depositRecordService;

    private static final Logger logger = LoggerFactory.getLogger("ROOT");


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String start(SysUser user) throws Exception {
        SysUser checkUser = sysUserService.checkUser(user.getUserId());
        Long balance = 0L;
        if (user.getUserName() == null){
            user.setUserName((user.getUserId()+"").replace("-", ""));
        }
        String userName = user.getUserName();
        if (checkUser == null) {
            // 生成新的钱包账户
            Account account = new Account();
            user.setCreateDate(DateUtils.currentSecond()).setBalance(1L).setSolBalance(BigDecimal.ZERO);
            user.setAddress(account.getPublicKey().toString()).setLanguages("ZN").setTax(new BigDecimal("0.08"));
            SecretKey secretKey = CryptoUtil.convertToSecretKey(jmKey);
            String encryptedKey = CryptoUtil.encrypt(Utils.bytesToHex(account.getSecretKey()), secretKey);
            MyWallet wallet = new MyWallet();
            wallet.setAddress(account.getPublicKey().toString()).setBaseKey(encryptedKey);
            wallet.setUserId(user.getUserId()).setUserName(user.getUserName());
            wallet.setBalance(BigDecimal.ZERO).setCreateDate(DateUtils.currentSecond());
            sysUserService.saveOrUpdate(user);
            walletService.saveOrUpdate(wallet);
            checkUser = new SysUser().setLanguages("ZN");
        } else {
            balance = checkUser.getBalance();
            userName = checkUser.getUserName();
        }
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return String.format(ActionEnum.START.getText(), balance.toString(), userName, userName);
        } else {
            return String.format(ActionZnEnum.START.getText(), balance.toString(), userName, userName);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setLanguage(Long userId,String code){
        SysUser checkUser = sysUserService.checkUser(userId);
        checkUser.setLanguages(code);
        return sysUserService.saveOrUpdate(checkUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String signIn(Long userId) {
        SysUser user = sysUserService.checkUser(userId);
        if (user == null) {
            return ActionZnEnum.FIRST_SEND.getText();

        }
        LocalDate today = LocalDate.now();
        if (user.getSignInDate() != null) {
            Instant instant = user.getSignInDate().toInstant();
            LocalDate signInDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            if (user.getSignInDate() != null && signInDate.isEqual(today)) {
                if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
                    return ActionEnum.SIGNIN_FAIL.getText();
                } else {
                    return ActionZnEnum.SIGNIN_FAIL.getText();
                }
            }
        }
        user.setBalance(user.getBalance() + 1).setSignInDate(new Date()).setUpdateDate(DateUtils.currentSecond());
        sysUserService.saveOrUpdate(user);
        if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
            return ActionEnum.SIGNIN_SUCCESS.getText();
        } else {
            return ActionZnEnum.SIGNIN_SUCCESS.getText();
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deposit(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser == null) {
            return ActionZnEnum.FIRST_SEND.getText();
        }
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return String.format(ActionEnum.DEPOSIT.getText(), checkUser.getAddress(), checkUser.getBalance().toString());
        } else {
            return String.format(ActionZnEnum.DEPOSIT.getText(), checkUser.getAddress(), checkUser.getBalance().toString());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String verifyDeposit(Long userId, String message) {
        String transactionId = "";
        if (message.startsWith("http://") || message.startsWith("https://")) {
            // 提取交易 ID
            String[] parts = message.split("/");
            transactionId = parts[parts.length - 1]; // 从 URL 的最后一部分获取交易 ID
        } else {
            transactionId = message;
        }

        SysUser user = sysUserService.checkUser(userId);

        logger.info("verifyDeposit111----");
        try {
            DepositRecord record = solanaService.verifyTx(transactionId);

            logger.info("verifyDeposit222----");
            if (record.getAmount().compareTo(BigDecimal.valueOf(0.1)) >= 0
                    && record.getReceiver().equals(user.getAddress())
                    && depositRecordService.checkTx(transactionId)) {

                logger.info("verifyDeposit333----");
//                BigDecimal money = solanaService.getBalance(user.getAddress());
//                if (money.compareTo(record.getAmount()) < 0) {
//                    if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
//                        return ActionEnum.VERIFY_FAILED.getText();
//                    } else {
//                        return ActionZnEnum.VERIFY_FAILED.getText();
//                    }
//                }
                BigDecimal time = record.getAmount().multiply(BigDecimal.valueOf(1000));
                Long times = time.setScale(0, BigDecimal.ROUND_DOWN).longValue();
                user.setBalance(user.getBalance() + times).setUpdateDate(DateUtils.currentSecond());
                record.setUpdateDate(DateUtils.currentSecond()).setUserId(userId).setUserName(user.getUserName());
                MyWallet wallet = walletService.getOne(userId);
                wallet.setBalance(wallet.getBalance().add(record.getAmount())).setUpdateDate(DateUtils.currentSecond());
                walletService.saveOrUpdate(wallet);
                depositRecordService.saveOrUpdate(record);
                sysUserService.saveOrUpdate(user);

                if (user.getLeader() != null) {
                    SysUser leader = sysUserService.getUser(user.getLeader());
                    leader.setSolBalance(leader.getSolBalance().add(record.getAmount().multiply(user.getTax()))).setUpdateDate(DateUtils.currentSecond());
                    sysUserService.saveOrUpdate(leader);
                }
                if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
                    return String.format(ActionEnum.DEPOSIT_SUCCESS.getText(), record.getAmount(), times);
                } else {
                    return String.format(ActionZnEnum.DEPOSIT_SUCCESS.getText(), record.getAmount(), times);
                }
            } else {
                if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
                    return ActionEnum.VERIFY_FAILED.getText();
                } else {
                    return ActionZnEnum.VERIFY_FAILED.getText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (user.getLanguages() == null || user.getLanguages().equals("EN")) {
                return ActionEnum.VERIFY_FAILED.getText();
            } else {
                return ActionZnEnum.VERIFY_FAILED.getText();
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String referral(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser == null) {
            return ActionZnEnum.FIRST_SEND.getText();
        }
        BigDecimal withdrawAmount = sysUserService.withdrawAmount(userId);
        int referralCount = sysUserService.referralCount(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return String.format(ActionEnum.REFERRAL.getText(), checkUser.getUserName(), checkUser.getSolBalance().toString()
                    , withdrawAmount.toString(), referralCount,checkUser.getTax().multiply(new BigDecimal(100))+"%");
        } else {
            return String.format(ActionZnEnum.REFERRAL.getText(), checkUser.getUserName(), checkUser.getSolBalance().toString()
                    , withdrawAmount.toString(), referralCount,checkUser.getTax().multiply(new BigDecimal(100))+"%");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String twData(Long userId, String twName) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser == null) {
            return ActionZnEnum.FIRST_SEND.getText();
        }
        if (checkUser.getBalance() < 1) {
            if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
                return ActionEnum.BALANCE_ZERO.getText();
            } else {
                return ActionZnEnum.BALANCE_ZERO.getText();
            }
        }
        String res = "";
        try {
            res = twService.GetName(twName,checkUser.getLanguages());
            checkUser.setBalance(checkUser.getBalance() - 1).setUpdateDate(DateUtils.currentSecond());
            sysUserService.saveOrUpdate(checkUser);
        } catch (Exception e) {
            if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
                res = ActionEnum.DM.getText();
            } else {
                res = ActionZnEnum.DM.getText();
            }
        }
        return res;
    }

    @Override
    public String help(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return ActionEnum.HELP.getText();
        } else {
            return ActionZnEnum.HELP.getText();
        }
    }

    @Override
    public String withdrawal_sol(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return ActionEnum.WITHDRAWAL_SOL.getText();
        } else {
            return ActionZnEnum.WITHDRAWAL_SOL.getText();
        }
    }

    @Override
    public String do_withdrawal(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return ActionEnum.DO_WITHDRAWAL.getText();
        } else {
            return ActionZnEnum.DO_WITHDRAWAL.getText();
        }
    }

    @Override
    public String twData(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return ActionEnum.TWDATA.getText();
        } else {
            return ActionZnEnum.TWDATA.getText();
        }
    }

    @Override
    public String waiting(Long userId) {
        SysUser checkUser = sysUserService.checkUser(userId);
        if (checkUser.getLanguages() == null || checkUser.getLanguages().equals("EN")) {
            return ActionEnum.WAIT.getText();
        } else {
            return ActionZnEnum.WAIT.getText();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTimes(String userName, int times) {
        SysUser user = sysUserService.getUser(userName);
        user.setBalance(user.getBalance()+times);
        return sysUserService.saveOrUpdate(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setTax(String userName, String tax) {
        SysUser user = sysUserService.getUser(userName);
        user.setTax(new BigDecimal(tax));
        return sysUserService.saveOrUpdate(user);
    }


}

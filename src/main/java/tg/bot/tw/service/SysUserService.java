package tg.bot.tw.service;

import tg.bot.tw.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * sys_user 服务类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 获取用户
     */
    SysUser checkUser(Long userId);

    /**
     * 获取用户
     */
    SysUser getUser(String userName);

    /**
     * 累计邀请
     */
    Integer referralCount(Long userId);


    /**
     * 累计提现
     */
    BigDecimal withdrawAmount(Long userId);

}

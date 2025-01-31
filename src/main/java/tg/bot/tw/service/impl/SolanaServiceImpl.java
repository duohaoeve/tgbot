package tg.bot.tw.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.tw.entity.DepositRecord;
import tg.bot.tw.req.hlReq;
import tg.bot.tw.service.SolanaService;
import tg.bot.tw.utils.HTTP;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SolanaServiceImpl implements SolanaService {


    private static String nodeUrl = "https://api.helius.xyz/v0/transactions";

    private static final Logger logger = LoggerFactory.getLogger("ROOT");

    @Autowired
    private HTTP http;

    @Override
    public BigDecimal getBalance(String address) {
        // 创建一个 RPC 客户端，连接到 Solana 主网
//        RpcClient client = new RpcClient("https://api.mainnet-beta.solana.com");
        RpcClient client = new RpcClient("https://cold-hanni-fast-mainnet.helius-rpc.com/");

        // 替换为您要查询的账户地址
        PublicKey publicKey = new PublicKey(address);
        // 查询账户余额
        BigDecimal res = BigDecimal.ZERO;
        try {
            long balance = client.getApi().getBalance(publicKey);
            res = BigDecimal.valueOf(balance).divide(BigDecimal.valueOf(1000000000),5, RoundingMode.HALF_UP);
        } catch (Exception e) {
            logger.info("---------查询账户余额出错---------");
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public DepositRecord verifyTx(String tx) throws JsonProcessingException {

        try {
            List<String> list = Collections.singletonList(tx); // 创建只读列表
            hlReq req = new hlReq();
            req.setTransactions(list); // 设置交易列表
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(req);

            String result = http.POST(nodeUrl, jsonString);
            List<String> reslist = JSONArray.parseArray(result, String.class);
            JSONObject jsonObject = JSONObject.parseObject(reslist.get(0));
            Long timestamp = jsonObject.getLong("timestamp");

            System.out.println("timestamp:" + timestamp);
            List<String> natlist = JSONArray.parseArray(jsonObject.getString("nativeTransfers"), String.class);
            JSONObject nativeTransfer = JSONObject.parseObject(natlist.get(0));

            String fromUserAccount = nativeTransfer.getString("fromUserAccount");
            String toUserAccount = nativeTransfer.getString("toUserAccount");
            ;
            Long amount = nativeTransfer.getLong("amount");
            BigDecimal amt = BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(1000000000), 5, RoundingMode.HALF_UP);

            DepositRecord res = new DepositRecord();
            res.setTx(tx).setAmount(amt).setSender(fromUserAccount).setReceiver(toUserAccount).setCreateDate(timestamp.intValue());

            return res;
        } catch (Exception e) {
            logger.info("-------verifyTx出错----------");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}

package tg.bot.tw.utils;

import lombok.Getter;
import net.i2p.crypto.eddsa.Utils;
import org.p2p.solanaj.core.Account;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SolanaUtil {



    // 创建 RPC 客户端
//        RpcClient client = new RpcClient("https://api.mainnet-beta.solana.com");


    public static void generateKey() throws Exception {
        // 生成新的钱包账户
        Account account = new Account(); // 生成账户地址和密钥

        // 打印账户的公钥和私钥
        System.out.println("Public Key: " + account.getPublicKey());
        System.out.println("Private Key: " + Utils.bytesToHex(account.getSecretKey()));
    }
}

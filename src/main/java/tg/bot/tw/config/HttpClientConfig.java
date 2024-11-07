package tg.bot.tw.config;


import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * http client配置
 *
 * @author shaohao
 */
@Configuration
@EnableAutoConfiguration
public class HttpClientConfig {

    @Value("${task.poxy.hostname}")
    private String hostname ;
    @Value("${task.poxy.port}")
    private int port;
    @Value("${task.poxy.userName}")
    private String userName ;
    @Value("${task.poxy.passWord}")
    private String passWord;

    //默认时间为秒

    private  Long connectTimeout=60000L;

    private Long readTimeout=60000L;

    private Long writeTimeout=60000L;
//    @Value("${http.maxIdleConnections}")
    private int maxIdleConnections=5;

    private Long keepAliveDurationNs=5L;
    //连接失败重试，默认打开开关 避免Unreachable IP addresses，和连接池满了拒绝请求情况
    private static boolean retryOnConnectionFailure = true;



    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDurationNs, TimeUnit.MINUTES);
        builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        builder.connectionPool(connectionPool);
//        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port)));
//        builder.proxyAuthenticator(proxyAuthenticator);

        //由于是服务队服务的 所以不用设置访问所有的HTTPS站点配置
        return builder.build();
    }
    // 创建 Authenticator 对象，提供用户名和密码
    Authenticator proxyAuthenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            // 设置账号和密码
            String credentials = Credentials.basic(userName, passWord);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credentials)
                    .build();
        }
    };

}


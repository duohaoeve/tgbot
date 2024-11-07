package tg.bot.tw.utils;


import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * 封装http协议，简化操作
 *
 * @author zwp
 */
@Service
public class HttpImpl implements HTTP {
    public final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    public final MediaType MEDIA_XML = MediaType.parse("application/xml; charset=utf-8");

    //最大尝试次数
    @Value("${task.http.maxRetryCount}")
    private int maxRetryCount;

    //hltoken
    @Value("${crypto.sol.hlKey}")
    private String key;

    //成功状态码
    @Value("${task.http.expectedStatusCode}")
    private int expectedStatusCode;

    @Autowired
    private  OkHttpClient httpClient = new OkHttpClient();

    /**
     *
     * GET极简同步方法
     *
     * @author liuyi 2016年7月17日
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public  String GET(String url, String token) throws HttpException,IOException {
        Request request = new Request.Builder()
                .url(url).get()
                .header("Authorization",token)
                .build();
        String result  =ReqExecute(request);

        return result;
    }


    @Override
    public String POST(String baseUrl, String jsonBody) {

        //拼装param
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
            urlBuilder.addQueryParameter("api-key", key);
        HttpUrl httpUrl= urlBuilder.build();

        RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
        Request request = new Request.Builder()
                .url(httpUrl.toString())
                .header("Content-Type","application/json")
                .header("Accept","*/*")
                .post(body)
                .build();


        return ReqExecute(request);
    }

    @Override
    public String postJson(String baseUrl, String jsonBody, String token) throws IOException {

        RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl)
//                .header("Authorization",token)
                .header("x-api-key",token)
                .post(body)
                .build();
        String result = ReqExecute(request);

        return result;
    }

    public void sleep(int second) {
        try {
            Thread.sleep(1000*second); // 休眠十秒（10000毫秒）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 请求方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public  String ReqExecute(Request request){
        int currentRetryCount = 0;
        String result = "";
        while (currentRetryCount < maxRetryCount) {
            try {
                Response response = ReqExecuteCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                // 处理请求异常，记录日志等
                currentRetryCount++;
                if (currentRetryCount < maxRetryCount) {
                    sleep(1);
                }
            }
        }

        return result;
    }

    /**
     *
     * 构造CALL方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @return
     */
    @Override
    public  Call ReqExecuteCall(Request request){
        return httpClient.newCall(request);
    }

}

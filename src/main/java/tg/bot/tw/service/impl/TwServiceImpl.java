package tg.bot.tw.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.tw.dto.twDto;
import tg.bot.tw.req.twReq;
import tg.bot.tw.service.TwService;
import tg.bot.tw.utils.HTTP;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class TwServiceImpl implements TwService {

    @Autowired
    private HTTP http;

    private static final Logger logger = LoggerFactory.getLogger("MyLogger");
    private static final Logger  myLogger = LoggerFactory.getLogger(TwServiceImpl.class);
    private static final String url = "toto.oz.xyz/api/metadata/get_past_usernames";
    private static final String botToken = "8170478387:AAFzBnra_DGYvYAUkTA3seDF_gCFhvDDfko";
    private static final String key = "81ca4bd673808b121b1d4a4e4a9cb427";



    @Override
    public boolean test(String token, String wallet) {
        return false;
    }


    @Override
    public String GetName(String userName) {
        twReq req = new twReq();
        req.setUser(userName);
        String body = JSONObject.toJSONString(req);
        logger.info("-获取历史用户名-"+userName);
        try {
            String result = http.postJson(url, body, key);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String data = jsonObject.getString("data");

            List<twDto> twList = JSONArray.parseArray(data, twDto.class);
            String usernames = twList.stream()
                    .map(twDto::getUsername)
                    .collect(Collectors.joining(", "));
            return usernames;
        } catch (IOException e) {
            logger.error("获取历史用户名请求异常",e);
            return null;
        }
    }


    public void sleep(int second) {
        try {
            Thread.sleep(1000*second); // 休眠十秒（10000毫秒）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

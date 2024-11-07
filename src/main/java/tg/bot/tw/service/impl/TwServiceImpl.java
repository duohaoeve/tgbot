package tg.bot.tw.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${crypto.sol.url}")
    private String url;
    @Value("${crypto.sol.key}")
    private String key;


    @Override
    public boolean test(String token, String wallet) {
        return false;
    }


    @Override
    public String GetName(String userName) {
        twReq req = new twReq();
        req.setUser(userName);
        String body = JSONObject.toJSONString(req);
        try {
            String result = http.postJson(url, body, key);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String data = jsonObject.getString("data");

            List<twDto> twList = JSONArray.parseArray(data, twDto.class);
            String usernames = twList.stream()
                    .map(twDto::getUsername)
                    .collect(Collectors.joining(", "));
            String res = "推特改名次数:"+twList.size()+"次。\n"+usernames;
            return res;
        } catch (IOException e) {
            return "获取历史用户名请求异常";
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

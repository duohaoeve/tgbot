package tg.bot.tw.service.impl;

import org.springframework.stereotype.Service;
import tg.bot.tw.service.ActionService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class ActionServiceImpl  implements ActionService {
    @Override
    public boolean test(String token, String wallet) {
        return false;
    }

}

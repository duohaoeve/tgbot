package tg.bot.tw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import tg.bot.tw.entity.MyWallet;
import tg.bot.tw.mapper.MyWalletMapper;
import tg.bot.tw.service.MyWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * my_wallet 服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class MyWalletServiceImpl extends ServiceImpl<MyWalletMapper, MyWallet> implements MyWalletService {

    @Override
    public MyWallet getOne(Long userId) {
        QueryWrapper<MyWallet> wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }
}

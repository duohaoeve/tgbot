package tg.bot.tw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import tg.bot.tw.entity.DepositRecord;
import tg.bot.tw.entity.SysUser;
import tg.bot.tw.mapper.SysUserMapper;
import tg.bot.tw.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * sys_user 服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Override
    public SysUser checkUser(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public SysUser getUser(String userName){
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.eq("user_name", userName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer referralCount(Long userId) {
        SysUser user = baseMapper.selectById(userId);
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.eq("leader", user.getUserName());
        int count = baseMapper.selectCount(wrapper);
        return count;
    }

    @Override
    public BigDecimal withdrawAmount(Long userId) {
        return BigDecimal.ZERO;
    }
}

package tg.bot.tw.service.impl;

import tg.bot.tw.entity.SysUser;
import tg.bot.tw.mapper.SysUserMapper;
import tg.bot.tw.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}

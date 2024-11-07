package tg.bot.tw.service;

import tg.bot.tw.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * sys_user 服务类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
public interface SysUserService extends IService<SysUser> {

    SysUser checkUser(Long userId);

}

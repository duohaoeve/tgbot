package tg.bot.tw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tg.bot.tw.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * sys_user Mapper 接口
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}

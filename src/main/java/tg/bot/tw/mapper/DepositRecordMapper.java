package tg.bot.tw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tg.bot.tw.entity.DepositRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * deposit_record Mapper 接口
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Repository
@Mapper
public interface DepositRecordMapper extends BaseMapper<DepositRecord> {

}

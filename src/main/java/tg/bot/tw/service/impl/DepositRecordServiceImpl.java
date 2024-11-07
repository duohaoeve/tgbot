package tg.bot.tw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import tg.bot.tw.entity.DepositRecord;
import tg.bot.tw.mapper.DepositRecordMapper;
import tg.bot.tw.service.DepositRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * deposit_record 服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Service
public class DepositRecordServiceImpl extends ServiceImpl<DepositRecordMapper, DepositRecord> implements DepositRecordService {

    @Override
    public boolean checkTx(String tx){
        QueryWrapper<DepositRecord> wrapper = new QueryWrapper();
        wrapper.eq("tx", tx);
        int count = baseMapper.selectCount(wrapper);
        if (count == 0){
            return true;
        }
        return false;
    };

}

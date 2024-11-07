package tg.bot.tw.service;

import tg.bot.tw.entity.DepositRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * deposit_record 服务类
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
public interface DepositRecordService extends IService<DepositRecord> {

    boolean checkTx(String tx);
}

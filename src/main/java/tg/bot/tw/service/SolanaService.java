package tg.bot.tw.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import tg.bot.tw.entity.DepositRecord;

import java.math.BigDecimal;

public interface SolanaService {

    BigDecimal getBalance(String address);

    DepositRecord verifyTx(String tx) throws JsonProcessingException;
}

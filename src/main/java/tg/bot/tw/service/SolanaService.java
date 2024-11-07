package tg.bot.tw.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;

public interface SolanaService {

    BigDecimal getBalance(String address);

    String verifyTx(String tx) throws JsonProcessingException;
}

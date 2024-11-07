package tg.bot.tw.service;

import tg.bot.tw.entity.SysUser;

public interface ActionService {

    String start(SysUser user) throws Exception;
    String deposit(Long userId);

    String verifyDeposit(Long userId,String message);
}

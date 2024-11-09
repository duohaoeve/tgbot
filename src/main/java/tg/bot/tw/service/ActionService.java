package tg.bot.tw.service;

import tg.bot.tw.entity.SysUser;

public interface ActionService {

    String start(SysUser user) throws Exception;

    boolean setLanguage(Long userId,String code);

    String signIn(Long userId);

    String deposit(Long userId);

    String verifyDeposit(Long userId,String message);

    String referral(Long userId);


    String twData(Long userId,String twName);

    String help(Long userId);

    String withdrawal_sol(Long userId);

    String do_withdrawal(Long userId);

    String twData(Long userId);

    String waiting(Long userId);


    boolean addTimes(String userName,int times);

    boolean setTax(String userName,String tax);



}

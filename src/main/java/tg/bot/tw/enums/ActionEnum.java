package tg.bot.tw.enums;


public enum ActionEnum {
    /**
     * World of fairy
     */
//    START("START", "\uD83D\uDE80 TwDataBot: Your Gateway to MeMe DeFi \uD83E\uDD16\n" +
//            "     Telegram | [Twitter](https://x.com/shaohaoxz) | Website \n" +
//            "    \n" +
//            "    ⬩ Balance: %s times\n"+
//            "    \n" +
//            "     Your referral link: https://t.me/twdata_bot?start=%s"),

    START("START", "\uD83D\uDE80 TwDataBot: Your Gateway to MeMe DeFi \uD83E\uDD16  \n" +
            "       [Twitter](https://x.com/shaohaoxz) | [Support](https://t.me/shaohao_gem)   \n" +
            "\n" +
            "⬩ Balance: %s times  \n" +
            "\n" +
            "Your referral link: [https://t.me/twdata_bot?start=%s](https://t.me/twdata_bot?start=%s)\n" +
            "\n" +
            "Please send a Twitter username, format:'@username', get Twitter username history,for example:  \n" +
            "@shaohaoxz" +
            "\n" +
            "\n" +
            "Send /help get help."),

    SIGNIN_FAIL("SIGNIN_FAIL", "今天你已经签到过了!"),

    SIGNIN_SUCCESS("SIGNIN_SUCCESS", "签到成功！次数+1"),

    DEPOSIT("DEPOSIT", "Deposite solana address:\n" +
            " %s" +
            "\n" +
            "\n" +
            "Price: 0.1SOL=100times.\n" +
            "\n" +
            "Your balance: %s times \n" +
            "\n" +
            "Tip: Minimum deposit amount 0.1sol, please transfer the SOL to this address, send transaction ID after successful transfer."),

    REFERRAL("REFERRAL", "\uD83D\uDD17 Invitation link: https://t.me/twdata_bot?start=%s\n" +
            "\uD83D\uDCB5 Withdrawable: %s SOL\n" +
            "\uD83D\uDCB0 Accumulated withdrawal: %s SOL\n" +
            "\uD83D\uDC65 Cumulative invitations: %s people\n" +
            "\uD83D\uDCD6 Rule:\n" +
            "1. Inviting others to use can earn them 8%% of their deposit permanently\n" +
            "2. Withdrawals can be made after reaching 0.1, and can only be applied for once every 24 hours. Trigger automatic payment to the receiving address at 8:00 am (UTC+8) every day, and the payment will be received within 24 hours after triggering."),

    HELP("HELP", "Support commands:\n" +
            "/start - Query your account\n" +
            "/qd - Daily sign-in\n" +
            "/deposit- Deposite for your account\n" +
            "/twdata- Search Twitter data\n" +
            "/referral - Referral rewards\n" +
            "/help - Tutorial & Help\n" +  "\n" +
            " [Twitter](https://x.com/shaohaoxz) | [Support](https://t.me/shaohao_gem)") ,

    FIRST_SEND("FIRST_SEND", "Please send /start first."),
    BALANCE_ZERO("BALANCE_ZERO", "Sorry, your credit is running zero, please /deposit first."),
    DM("DM", "Sorry, there is a problem here, please dm Admin."),

    TWDATA("TWDATA", ( "Please send a Twitter username, for example:  " +
            "\n" +
            "@shaohaoxz")),

    TWTEXT("TWTEXT", ( "推特改名次数: %s次。  " +
            "\n" +
            "%s")),
    TWFAIL("TWFAIL", ( "获取历史用户名请求异常")),

    TW_NO_DATA("TW_NO_DATA", ( "未获取到有效的推特用户。"))
            ;

    private String action;
    private String text;

    ActionEnum(String action, String text) {
        this.action = action;
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

}

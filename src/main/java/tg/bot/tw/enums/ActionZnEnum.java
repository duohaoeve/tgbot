package tg.bot.tw.enums;


public enum ActionZnEnum {

    START("START", "\uD83D\uDE80 TwDataBot: 你的 MeMe DeFi 通道 \uD83E\uDD16  \n" +
            "       [Twitter](https://x.com/shaohaoxz) | [支持](https://t.me/shaohao_gem)   \n" +
            "\n" +
            "⬩ 余额: %s 次  \n" +
            "\n" +
            "你的推荐链接: [https://t.me/twdata_bot?start=%s](https://t.me/twdata_bot?start=%s)\n" +
            "\n" +
            "请发送 Twitter 用户名，格式：'@用户名'，以获取 Twitter 用户名历史，例如：  \n" +
            "@shaohaoxz" +
            "\n" +
            "\n" +
            "发送 /help 获取帮助。"),

    SIGNIN_FAIL("SIGNIN_FAIL", "你今天已经签到过了！"),

    SIGNIN_SUCCESS("SIGNIN_SUCCESS", "签到成功！次数 + 1"),

    DEPOSIT("DEPOSIT", "存款的 Solana 地址：\n" +
            " %s" +
            "\n" +
            "\n" +
            "价格: 0.1 SOL = 100 次。\n" +
            "\n" +
            "你的余额: %s 次 \n" +
            "\n" +
            "提示: 最小存款金额为 0.1 SOL，请将 SOL 转账至此地址，转账成功后发送交易 ID。"),

    REFERRAL("REFERRAL", "\uD83D\uDD17 邀请链接: https://t.me/twdata_bot?start=%s\n" +
            "\uD83D\uDCB5 可提现: %s SOL\n" +
            "\uD83D\uDCB0 累计提现: %s SOL\n" +
            "\uD83D\uDC65 累积邀请: %s 人\n" +
            "\uD83D\uDCD6 规则:\n" +
            "1. 邀请他人使用可以永久获得其存款的 8%% \n" +
            "2. 提现需达到 0.1，并且每 24 小时只能申请一次。每天上午 8:00（UTC+8）触发自动支付，付款将在 24 小时内到账。"),

    HELP("HELP", "支持的命令:\n" +
            "/start - 查询你的账户\n" +
            "/qd - 每日签到\n" +
            "/deposit - 为你的账户存款\n" +
            "/twdata - 查询 Twitter 数据\n" +
            "/referral - 推荐奖励\n" +
            "/help - 教程与帮助\n" +
            "\n" +
            " [Twitter](https://x.com/shaohaoxz) | [支持](https://t.me/shaohao_gem)"),

    FIRST_SEND("FIRST_SEND", "请先发送 /start。"),

    BALANCE_ZERO("BALANCE_ZERO", "对不起，你的余额为零，请先 /deposit。"),

    DM("DM", "对不起，这里出现了问题，请联系管理员。"),

    TWDATA("TWDATA", "请发送一个 Twitter 用户名，例如：  " +
            "\n" +
            "@shaohaoxz"),

    TWTEXT("TWTEXT", "Twitter 名称更改次数: %s 次。  " +
            "\n" +
            "%s"),

    TWFAIL("TWFAIL", "获取历史用户名请求时发生异常。"),

    TW_NO_DATA("TW_NO_DATA", "没有获取到有效的 Twitter 用户。"),

    WITHDRAWAL_SOL("WITHDRAWAL_SOL", "提现SOL。"),

    DO_WITHDRAWAL("DO_WITHDRAWAL", "提现功能正在开发中。" + "\n" +
            "[支持](https://t.me/shaohao_gem)。"),

    DEPOSIT_SUCCESS("DEPOSIT_SUCCESS", "存款成功，金额 = %s SOL，次数 + %s。"),

    VERIFY_FAILED("VERIFY_FAILED", "验证失败。"),

    WAIT("WAIT", ( "请稍等。"))

            ;

    private String action;
    private String text;

    ActionZnEnum(String action, String text) {
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

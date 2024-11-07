package tg.bot.tw.service.bots;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import tg.bot.tw.entity.SysUser;
import tg.bot.tw.enums.ActionEnum;
import tg.bot.tw.service.ActionService;
import tg.bot.tw.service.SolanaService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Component
public class TwBot implements SpringLongPollingBot,LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    private final Map<Long, String> userStates = new HashMap<>(); // 用于保存用户状态

    @Value("${tg.twbot.token}")
    private String botToken;

    @Autowired
    private SolanaService solanaService;

    @Autowired
    private ActionService actionService;

    @Autowired
    public TwBot(@Value("${tg.twbot.token}") String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        long chatId = update.getMessage().getChatId();

        // 检查更新是否包含消息
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long user_id = update.getMessage().getChat().getId();
            // 检查是否是/start命令
            if (messageText.startsWith("/start")) {
                String[] parts = messageText.split(" ");
                String inviteCode = "";
                if (parts.length > 1) {
                    inviteCode = parts[1];
                }
                String user_username = update.getMessage().getChat().getUserName();
                SysUser user = new SysUser();
                user.setUserId(user_id).setUserName(user_username).setLeader(inviteCode);
                try {
                    SendMessage message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(actionService.start(user))
                            .build();
                    telegramClient.execute(message); // 发送消息
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            if (messageText.equals("/deposit")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.deposit(user_id))
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                    userStates.put(chatId, "awaitDeposit"); // 更新用户状态
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (userStates.containsKey(chatId) && userStates.get(chatId).equals("awaitDeposit")) {
                if (messageText.length()<=50){
                    return ;
                }
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.verifyDeposit(user_id,messageText))
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                    // 清除状态
                    userStates.remove(chatId);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void getBalanceHandle(long chatId, String response) {

        BigDecimal amount = solanaService.getBalance(response);

        // 可以发送感谢或确认消息
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text("Your balance: " + amount)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




}

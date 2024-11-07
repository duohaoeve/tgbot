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

            // 检查是否是/start命令
            if (messageText.startsWith("/start")) {
                String[] parts = messageText.split(" ");
                if (parts.length > 1) {
                    String inviteCode = parts[1]; // 获取haigexz
                    // 处理你的 inviteCode
                    handleInviteCode(inviteCode, chatId);
                }
            }

            if (messageText.equals("/getBalance")) {
                String replyMessageText = "Please send a address."; // 响应消息文本
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(replyMessageText)
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                    userStates.put(chatId, "awaitgetBalance"); // 更新用户状态
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (userStates.containsKey(chatId) && userStates.get(chatId).equals("awaitgetBalance")) {
                // 用户的回复处理
                String userResponse = messageText;
                getBalanceHandle(chatId, userResponse);

                // 清除状态
                userStates.remove(chatId);
            } else if (messageText.equals("/verifyTx")) {
                String replyMessageText = "Please send a tx."; // 响应消息文本
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(replyMessageText)
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                    userStates.put(chatId, "awaitverifyTx"); // 更新用户状态
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (userStates.containsKey(chatId) && userStates.get(chatId).equals("awaitverifyTx")) {
                // 用户的回复处理
                String userResponse = messageText;
                try {
                    verifyTxHandle(chatId, userResponse);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                // 清除状态
                userStates.remove(chatId);
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

    private void verifyTxHandle(long chatId, String response) throws JsonProcessingException {

        String res = solanaService.verifyTx(response);

        // 可以发送感谢或确认消息
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(res)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleInviteCode(String inviteCode, long chatId) {

        String res = "Your inviteCode:"+inviteCode;

        // 可以发送感谢或确认消息
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(res)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

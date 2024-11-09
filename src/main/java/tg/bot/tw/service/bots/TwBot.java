package tg.bot.tw.service.bots;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import tg.bot.tw.entity.SysUser;
import tg.bot.tw.enums.ActionEnum;
import tg.bot.tw.service.ActionService;
import tg.bot.tw.service.SolanaService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.toIntExact;


@Component
public class TwBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    private final Map<Long, String> userStates = new HashMap<>(); // 用于保存用户状态

    @Value("${tg.twbot.token}")
    private String botToken;

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

        // 检查更新是否包含消息
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            long user_id = update.getMessage().getChat().getId();
            String username = update.getMessage().getChat().getUserName();
            // 检查是否是/start命令
            if (messageText.startsWith("/start")) {

                do_start(chatId,user_id,messageText,username);

            } else if (messageText.equals("/qd")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.signIn(user_id))
//                        .parseMode(ParseMode.MARKDOWN)
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }  else if (messageText.equals("/deposit")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.deposit(user_id))
//                        .parseMode(ParseMode.MARKDOWN)
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                    userStates.put(chatId, "awaitDeposit"); // 更新用户状态
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.equals("/twdata")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text( actionService.twData(user_id))
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.equals("/referral")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.referral(user_id))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(InlineKeyboardButton
                                                .builder()
                                                .text(actionService.withdrawal_sol(user_id))
                                                .callbackData("WITHDRAWAL_SOL")
                                                .build()
                                        )
                                ).build())
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (messageText.equals("/help")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(actionService.help(user_id))
                        .parseMode(ParseMode.MARKDOWN)
                        .build();

                try {
                    telegramClient.execute(message); // 发送消息
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else{
                if (messageText.startsWith("@")) {
                    String twName = messageText.substring(1);
                    Integer replyToMessageId = update.getMessage().getMessageId();
                    SendMessage message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(actionService.twData(user_id,twName))
                            .replyToMessageId(replyToMessageId)
                            .build();
                    try {
                        telegramClient.execute(message); // 发送消息
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(username.equals("haigexz")){
                        if (messageText.startsWith("add")) {
                            String[] admin = messageText.split("-");
                            String userName =  admin[1];
                            int times = Integer.parseInt(admin[2]);
                            SendMessage message = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text(actionService.addTimes(userName,times)+"")
                                    .build();
                            try {
                                telegramClient.execute(message); // 发送消息
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }

                        }else if (messageText.startsWith("tax")) {
                            String[] admin = messageText.split("-");
                            String userName = admin[1];
                            String tax =  admin[2];
                            SendMessage message = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text(actionService.setTax(userName,tax)+"")
                                    .build();
                            try {
                                telegramClient.execute(message); // 发送消息
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

            if (userStates.containsKey(chatId) && userStates.get(chatId).equals("awaitDeposit")) {
                if (messageText.length() <= 50) {
                    return;
                }

                try {
                    SendMessage waitMessage = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(actionService.waiting(user_id))
                            .build();

                    telegramClient.execute(waitMessage); // 发送消息

                    SendMessage message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(actionService.verifyDeposit(user_id, messageText))
                            .build();

                    telegramClient.execute(message); // 发送消息
                    // 清除状态
                    userStates.remove(chatId);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }



        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            long user_id = update.getCallbackQuery().getFrom().getId();
            String username = update.getCallbackQuery().getFrom().getUserName();
            if (call_data.equals("ZN")) {
                if (actionService.setLanguage(user_id,"ZN")){
                    do_start(chat_id,user_id,null,username);
                }
            }else if (call_data.equals("EN")) {
                if (actionService.setLanguage(user_id,"EN")){
                    do_start(chat_id,user_id,null,username);
                }
            }else if (call_data.equals("WITHDRAWAL_SOL")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(actionService.do_withdrawal(user_id))
                        .parseMode(ParseMode.MARKDOWN)
                        .build();
                try {
                    telegramClient.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void do_start(long chatId,long user_id,String messageText,String username){


        SysUser user = new SysUser();
        user.setUserId(user_id).setUserName(username);
        if (messageText != null) {
            String[] parts = messageText.split(" ");
            String inviteCode = "";
            if (parts.length > 1) {
                inviteCode = parts[1];
            }
            user.setLeader(inviteCode);
        }

        try {
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(actionService.start(user))
                    .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("中文")
                                            .callbackData("ZN")
                                            .build(),InlineKeyboardButton
                                            .builder()
                                            .text("English")
                                            .callbackData("EN")
                                            .build()
                                    )
                            ).build())
                    .parseMode(ParseMode.MARKDOWN)
                    .build();
            telegramClient.execute(message); // 发送消息
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

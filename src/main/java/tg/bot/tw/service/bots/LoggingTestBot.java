package tg.bot.tw.service.bots;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingTestBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;

    public LoggingTestBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            // Set variables
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage message = SendMessage // Create a message object object
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();
            log(user_first_name, user_last_name, Long.toString(user_id), user_username,message_text, message_text);

            Message imessage = update.getMessage();
            User user = imessage.getFrom();
            Long userId = user.getId();
            String userName = user.getUserName();
            String languageCode = user.getLanguageCode();
            System.out.println("mytest answer: \n Text - " + userId+" "+userName
            +" "+ languageCode);

            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }



    private void log(String first_name, String last_name, String user_id,String user_username,String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("user_username: \n Text - " + user_username);
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }

    //检查群组
//    if (update.hasMessage()) {
//        Message message = update.getMessage();
//
//        if (message.isNewChatMembers() || message.isGroupChat() || message.isChannelChat()) {
//            // 这是来自群组/频道的消息，执行所需的逻辑
//            // 在这里可以选择不响应，或者执行不同的操作
//        } else {
//            // 这是来自个人聊天的消息，执行正常的逻辑
//            // 可以添加可点击的按钮或者其他操作
//        }
//    }

}

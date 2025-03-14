package tg.bot.tw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        String botToken = "8170478387:AAFzBnra_DGYvYAUkTA3seDF_gCFhvDDfko";
//        //简单回复
//        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
//            botsApplication.registerBot(botToken, new MyAmazingBot());
//            System.out.println("MyAmazingBot successfully started!");
//            Thread.currentThread().join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //照片
//        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
//            botsApplication.registerBot(botToken, new PhotoBot(botToken));
//            System.out.println("PhotoBot successfully started!");
//            Thread.currentThread().join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //日志
//        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
//            botsApplication.registerBot(botToken, new EmojiTestBot(botToken));
//            System.out.println("LoggingTestBot successfully started!");
//            Thread.currentThread().join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

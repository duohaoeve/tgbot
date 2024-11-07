package tg.bot.tw.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class twDto {

    private String username;
    private Date last_checked;
}

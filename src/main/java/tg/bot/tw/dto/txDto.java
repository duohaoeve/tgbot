package tg.bot.tw.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class txDto {


    private String sender;
    private String receiver;
    private BigDecimal amount;
}

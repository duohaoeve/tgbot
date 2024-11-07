package tg.bot.tw.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class twReq {

    private String user;

    private String how ="username";
    private int page =1;
    private String since =null;
}

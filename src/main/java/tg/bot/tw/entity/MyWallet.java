package tg.bot.tw.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * my_wallet
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("my_wallet")
@Accessors(chain = true)
public class MyWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 私钥
     */
    private String baseKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Integer createDate;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 更新时间
     */
    private Integer updateDate;


}

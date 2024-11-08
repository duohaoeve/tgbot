package tg.bot.tw.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * sys_user
 * </p>
 *
 * @author shaohao
 * @since 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 余额
     */
    private Long balance;

    /**
     * sol余额
     */
    private BigDecimal solBalance;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 上级
     */
    private String leader;


    /**
     * 签到时间
     */
    private Date signInDate;

    /**
     * 语言
     */
    private String languages;

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

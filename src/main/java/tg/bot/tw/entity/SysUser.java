package tg.bot.tw.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 余额
     */
    private String balance;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 上级
     */
    private String leader;

    /**
     * 备注
     */
    private Double remark;

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

package com.ysu.his.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class RegistLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 是否有效，1 有效，0 失效
     */
    private Integer active;

     /**
     * 对应的角色Id
     */
    private Integer roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;


}

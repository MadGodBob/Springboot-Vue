package com.wms.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 库存物品表
 * </p>
 *
 * @author pengbo
 * @since 2026-04-01
 */
@Data
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物品编号（唯一）
     */
    private String itemId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 备注
     */
    private String remark;
}

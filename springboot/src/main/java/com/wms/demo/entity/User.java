package com.wms.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId(type = IdType.AUTO)  // 使用数据库自增策略
    private Integer id;
    private String no;
    private String name;
    private String password;
    private Integer sex;
    private Integer roleId;
    private String phone;
    @TableField("isValid")
    private String isValid;
}

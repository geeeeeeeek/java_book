package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("b_borrow")
public class Borrow implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String status;
    @TableField
    public String borrowTime;
    @TableField
    public String returnTime;
    @TableField
    public String expectTime;
    @TableField
    public String thingId;
    @TableField
    public String userId;
    @TableField
    public String hasDelayed;

    @TableField(exist = false)
    public String username; // 用户名
    @TableField(exist = false)
    public String title; // 商品名称
    @TableField(exist = false)
    public String cover; // 商品封面

}

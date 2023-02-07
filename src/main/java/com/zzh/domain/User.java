package com.zzh.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {
    //将id进行序列化。解决精度失真问题
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private String userName;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String address;
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
    private String avatorUrl;
}

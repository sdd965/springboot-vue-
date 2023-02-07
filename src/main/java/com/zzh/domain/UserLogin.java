package com.zzh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    private String account;
    private String passWord;
    private String nickname;
    private String confirm;
    private String avatar;
    private String token;
}

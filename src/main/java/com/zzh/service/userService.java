package com.zzh.service;

import cn.hutool.http.server.HttpServerResponse;
import com.zzh.domain.User;
import com.zzh.domain.UserLogin;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface userService {
    List<User> selectAll();

    boolean insert(User user);

    void  export(HttpServletResponse response) throws IOException;
    boolean deleteById(Long id);

    boolean update(User user);

    Map<String, Object> selectByPage(Integer current, Integer pageSize, String userName, String phone, String address);

    boolean deleteItems(List<Long> list);

    void imp(MultipartFile multipartFile) throws IOException;

    UserLogin login(UserLogin userLogin);

    boolean register(UserLogin userLogin);
}

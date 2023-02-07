package com.zzh.controller;

import com.zzh.domain.User;
import com.zzh.domain.UserLogin;
import com.zzh.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private userService userService;

    @PostMapping
    public Result insert(@RequestBody User user)
    {
        boolean flag = userService.insert(user);
        return new Result(flag?Code.SAVE_OK:Code.SAVE_ERR,flag?"插入成功":"插入失败");
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserLogin userLogin)
    {
        UserLogin user = userService.login(userLogin);
        return new Result(user.getAccount()!=null?Code.LOGIN_OK:Code.LOGIN_ERR,user,user.getAccount()!=null?"登陆成功":"登陆失败,用户名或密码错误");
    }

    @GetMapping("/export")
    void export(HttpServletResponse response) throws IOException {
        userService.export(response);
    }

    @PutMapping
    public Result update(@RequestBody User user)
    {
        boolean flag = userService.update(user);
        return new Result(flag?Code.UPDATE_OK:Code.UPDATE_ERR,flag?"更新成功":"更新失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id)
    {
        boolean flag = userService.deleteById(id);
        return new Result(flag?Code.DELETE_OK:Code.DELETE_ERR,flag?"删除成功":"删除的对象不存在");
    }

    @DeleteMapping("/items")
    public Result deleteItems(@RequestBody List<Long> list)
    {
        boolean flag = userService.deleteItems(list);
        return new Result(flag?Code.DELETE_OK:Code.DELETE_ERR,flag?"删除成功":"删除的对象不存在");

    }

    @GetMapping()
    public Map<String, Object> getPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String userName
    , @RequestParam String phone, @RequestParam String address)
    {
        return userService.selectByPage(pageNum,pageSize,userName,phone,address);
    }

    @PostMapping("/import")
    void  imp(MultipartFile file) throws IOException {
        userService.imp(file);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserLogin userLogin)
    {
        boolean flag = userService.register(userLogin);
        return new Result(flag?Code.REGISTER_OK:Code.REGISTER_ERR,flag?"注册成功":"注册失败,用户名已占用");
    }

}

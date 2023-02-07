package com.zzh.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.controller.Code;
import com.zzh.dao.userDao;
import com.zzh.domain.User;
import com.zzh.domain.UserLogin;
import com.zzh.exception.BusinessException;
import com.zzh.exception.SystemException;
import com.zzh.service.userService;
import com.zzh.utils.tokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class userServiceImpl implements userService {
    @Resource
    private tokenUtil  tokenUtil;
    @Autowired
    private userDao userDao;

    @Override
    public List<User> selectAll() {
        return userDao.selectList(null);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        List<User> users = userDao.selectList(null);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(users, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename"+fileName+".xlsx");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        writer.flush(servletOutputStream,true);
        servletOutputStream.close();
        writer.close();
    }

    @Override
    public void imp(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        List<User> list = excelReader.readAll(User.class);
        list.forEach(user -> userDao.insert(user));
        System.out.println(list);
        inputStream.close();
        excelReader.close();
    }

    @Override
    public boolean update(User user) {
        try {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getId,user.getId());
            return userDao.update(user,lambdaQueryWrapper)>0;
        }catch (Exception exception)
        {
            throw new BusinessException(Code.UPDATE_ERR,"用户输入的参数有误");
        }
    }

    @Override
    public boolean deleteById(Long id) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,id);
        return userDao.delete(lambdaQueryWrapper)>0;
    }

    @Override
    public boolean insert(User user) {
        try {
            return userDao.insert(user)>0;
        }catch (Exception exception)
        {
            throw new BusinessException(Code.SAVE_ERR,"用户输入的参数有误");
        }
    }

    @Override
    public Map<String, Object> selectByPage(Integer current, Integer pageSize, String userName, String phone, String address) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(userName!=null,User::getUserName,userName);
        lambdaQueryWrapper.like(phone!=null,User::getPhone,phone);
        lambdaQueryWrapper.like(address!=null,User::getAddress,address);
        IPage page = new Page(current, pageSize);
        userDao.selectPage(page, lambdaQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("data",page.getRecords());
        return map;
    }

    @Override
    public boolean deleteItems(List<Long> list) {
        return userDao.deleteBatchIds(list)>0;
    }

    @Override
    public UserLogin login(UserLogin userLogin) {
        try {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserName, userLogin.getAccount());
            lambdaQueryWrapper.eq(User::getPassword, userLogin.getPassWord());
            User user = userDao.selectOne(lambdaQueryWrapper);
            UserLogin userLogin1 = new UserLogin();
            if (user != null) {
                userLogin1.setAccount(user.getUserName());
                userLogin1.setNickname(user.getNickname());
                userLogin1.setAvatar(user.getAvatorUrl());
                String token = tokenUtil.createToken(userLogin1.getAccount());
                userLogin1.setToken(token);
            }
            return userLogin1;
        }catch (Exception exception)
        {
            throw new SystemException(Code.SYSTEM_ERR,exception.getMessage());
        }
    }

    @Override
    public boolean register(UserLogin userLogin) {
        if(!userLogin.getPassWord().equals(userLogin.getConfirm()))
        {
            throw new BusinessException(Code.BUSINESS_ERR, "用户两次输入的密码不一致");
        }else if(userLogin.getAccount().length()<3 ||userLogin.getAccount().length()>15 )
        {
            throw new BusinessException(Code.BUSINESS_ERR, "用户输入的用户名长度非法");}
        else
        {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserName,userLogin.getAccount());
            try{
                User user = userDao.selectOne(lambdaQueryWrapper);
                if(user == null)
                {
                    User user1 = new User();
                    user1.setUserName(userLogin.getAccount());
                    user1.setPassword(userLogin.getPassWord());
                    userDao.insert(user1);
                    return true;
                }
                else {
                    return false;
                }
            }catch (Exception e)
            {
                throw new SystemException(Code.SYSTEM_ERR, "数据库异常");
            }

        }


    }
}

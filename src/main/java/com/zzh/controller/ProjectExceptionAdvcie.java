package com.zzh.controller;

import com.zzh.exception.BusinessException;
import com.zzh.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvcie {
    @ExceptionHandler(SystemException.class)
    public Result doException(SystemException ex)
    {
        //记录日志
        //发消息给运维
        //返回消息
        System.out.println("捕获到了异常");
        return new Result(ex.getCode(),null, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doException(BusinessException ex)
    {
        return new Result(ex.getCode(),null, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex)
    {
        //记录日志
        //发消息给运维
        //返回消息
        return new Result(Code.UNKNOWN_ERR,null, ex.getMessage());
    }
}

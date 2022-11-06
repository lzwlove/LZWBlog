package com.lzw.handler.exception;

import com.lzw.domain.ResponseResult;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.exception.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //先把异常信息打印出来，方便测试
        log.error("出错了{}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME.getCode(), e.getMsg());
    }

    //除了已知的异常，还会有系统的异常无法预知
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        //先把异常信息打印出来，方便测试
        log.error("出错了{}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}

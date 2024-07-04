package org.myblog.global.exception;

import org.myblog.domain.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
    Spring 전역 예외 처리 메커니즘
        -- @ControllerAdvice 로 예외처리핸들러 정의
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex){
        return ex.getMessage(); // 404 상태코드와 함께 예외메세지 반환
    }
}

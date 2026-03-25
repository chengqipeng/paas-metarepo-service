package com.hongyang.platform.metarepo.service.common.exception;

import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.base.exception.BaseUnKnownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseKnownException.class)
    public ResponseEntity<Map<String, Object>> handleKnownException(BaseKnownException e) {
        log.warn("业务异常: code={}, msg={}", e.getErrorCode(), e.getErrorMsg());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "code", e.getErrorCode(),
                        "message", e.getErrorMsg()
                ));
    }

    @ExceptionHandler(BaseUnKnownException.class)
    public ResponseEntity<Map<String, Object>> handleUnKnownException(BaseUnKnownException e) {
        log.error("系统异常: code={}, msg={}", e.getErrorCode(), e.getErrorMsg(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "code", e.getErrorCode(),
                        "message", e.getErrorMsg()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("未捕获异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "code", 50001,
                        "message", "系统内部错误"
                ));
    }
}

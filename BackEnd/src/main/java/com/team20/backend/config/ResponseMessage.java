package com.team20.backend.config;

import com.fasterxml.jackson.annotation.JsonInclude;

// 响应对象
@JsonInclude(JsonInclude.Include.NON_NULL) // 忽略空值字段
public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

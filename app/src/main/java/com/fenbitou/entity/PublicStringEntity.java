package com.fenbitou.entity;

/**
 * Created by bishuang on 2017/7/25.
 * 字符串类型实体类
 */

public class PublicStringEntity {
    private String message;
    private boolean success;
    private String entity;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}

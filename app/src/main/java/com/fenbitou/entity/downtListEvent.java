package com.fenbitou.entity;

/**
 * Created by bishuang on 2017/7/24.
 * 下载选择页的eventBus
 */

public class downtListEvent {
    private String msg;

    public downtListEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

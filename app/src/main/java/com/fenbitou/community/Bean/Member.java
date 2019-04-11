package com.fenbitou.community.Bean;

/**
 * Created by Administrator on 2017/08/28.
 */

public class Member {
    private String title,name,avatar;

    public Member(String title, String name, String avatar) {
        this.title = title;
        this.name = name;
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

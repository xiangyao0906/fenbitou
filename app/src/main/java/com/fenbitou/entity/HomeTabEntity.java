package com.fenbitou.entity;

public class HomeTabEntity {
    private int tabIcon;
    private String tabText;

    public HomeTabEntity(int tabIcon, String tabText) {
        this.tabIcon = tabIcon;
        this.tabText = tabText;
    }

    public int getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(int tabIcon) {
        this.tabIcon = tabIcon;
    }

    public String getTabText() {
        return tabText;
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }
}

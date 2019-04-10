package com.yizhilu.base;

import android.app.Activity;

import com.yizhilu.utils.ILog;

import java.util.LinkedList;

/**
 * Created by ming on 2016/12/14 11:21.
 * Explain:activity栈
 */
public class ActivityStack {

    /**
     * 存activity的list，方便管理activity
     */
    public LinkedList<Activity> activityList = new LinkedList<Activity>();

    public ActivityStack() {
    }

    /**
     * 将Activity添加到activityList中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 获取栈顶Activity
     *
     * @return
     */
    public Activity getLastActivity() {
        return activityList.getLast();
    }

    /**
     * 将Activity移除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (null != activity && activityList.contains(activity)) {
            activityList.remove(activity);
            activity.finish();

        }
    }

    public void removeAllActivity() {
        activityList.removeAll(activityList);
    }

    /**
     * 判断某一Activity是否在运行
     *
     * @param className
     * @return
     */
    public boolean isActivityRunning(String className) {
        if (className != null) {
            for (Activity activity : activityList) {
                ILog.i(activity.getClass().getName());
                if (activity.getClass().getSimpleName().equals(className)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 退出所有的Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * finish传入的activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            ILog.i(activity.getClass().getName());
            activity.finish();
        }
    }

    /**
     * @author 杨财宾 时间:2015-8-29 方法说明:遍历所有Activity并finish
     */
    public void exit() {
        while (activityList.size() > 0) {
            Activity activity = activityList.get(activityList.size() - 1);
            activityList.remove(activityList.size() - 1);
            activity.finish();
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }


}

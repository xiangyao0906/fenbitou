package com.fenbitou.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;


import com.fenbitou.base.DemoApplication;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.AppUpdateUtils;
import com.fenbitou.utils.FileUtil;
import com.fenbitou.utils.HttpManager;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.ImageUtils;
import com.fenbitou.utils.UpdateAppHttpUtil;

import java.io.File;


/**
 * 后台下载
 */
public class DownloadService extends Service {

    private static final int NOTIFY_ID = 0;
    private static final String TAG = DownloadService.class.getSimpleName();
    public static boolean isRunning = false;
    private NotificationManager mNotificationManager;
    private DownloadBinder binder = new DownloadBinder();
    private NotificationCompat.Builder mBuilder;
    private boolean mDismissNotificationProgress = false;

    public static void bindService(Context context, ServiceConnection connection) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        isRunning = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 返回自定义的DownloadBinder实例
        return binder;
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        super.onDestroy();
    }

    /**
     * 创建通知
     */
    private void setUpNotification() {
        if (mDismissNotificationProgress) {
            return;
        }

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("开始下载")
                .setContentText("正在连接服务器")
                .setSmallIcon(R.drawable.logo).setLargeIcon(ImageUtils.drawableToBitmap(ImageUtils.getAppIcon(DownloadService.this)))
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * 下载模块
     */
    private void startDownload(final DownloadCallback callback) {
        String apkUrl = DemoApplication.downloadUrl;

        if (TextUtils.isEmpty(apkUrl)) {
            String contentText = "新版本下载路径错误";
            stop(contentText);
            return;
        }
        String appName =getResources().getString(R.string.apk_name)+".apk";
        String target = FileUtil.getDirPath("download")
                + File.separator;;
        File appDir = new File(target);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }



        new UpdateAppHttpUtil().download(apkUrl, target, appName, new FileDownloadCallBack(callback));

    }

    private void stop(String contentText) {
        if (mBuilder != null) {
            mBuilder.setContentTitle(AppUpdateUtils.getAppName(DownloadService.this)).setContentText(contentText);
            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(NOTIFY_ID, notification);
        }
        close();
    }

    private void close() {
        stopSelf();
        isRunning = false;
    }

    /**
     * 进度条回调接口
     */
    public interface DownloadCallback {
        /**
         * 开始
         */
        void onStart();

        /**
         * 进度
         *
         * @param progress  进度 0.00 -1.00 ，总大小
         * @param totalSize 总大小 单位B
         */
        void onProgress(float progress, long totalSize);

        /**
         * 总大小
         *
         * @param totalSize 单位B
         */
        void setMax(long totalSize);

        /**
         * 下载完了
         *
         * @param file 下载的app
         * @return true ：下载完自动跳到安装界面，false：则不进行安装
         */
        boolean onFinish(File file);

        /**
         * 下载异常
         *
         * @param msg 异常信息
         */
        void onError(String msg);
    }

    /**
     * DownloadBinder中定义了一些实用的方法
     *
     * @author user
     */
    public class DownloadBinder extends Binder {
        /**
         * 开始下载
         *
         * @param callback 下载回调
         */
        public void start(DownloadCallback callback) {
            //下载
            startDownload(callback);
        }
    }

    class FileDownloadCallBack implements HttpManager.FileCallback {
        private final DownloadCallback mCallBack;
        int oldRate = 0;

        public FileDownloadCallBack(@Nullable DownloadCallback callback) {
            super();
            this.mCallBack = callback;
        }

        @Override
        public void onBefore() {
            //初始化通知栏
            setUpNotification();
            if (mCallBack != null) {
                mCallBack.onStart();
            }
        }

        @Override
        public void onProgress(float progress, long total) {
            //做一下判断，防止自回调过于频繁，造成更新通知栏进度过于频繁，而出现卡顿的问题。
            int rate = Math.round(progress * 100);
            if (oldRate != rate) {
                if (mCallBack != null) {
                    mCallBack.setMax(total);
                    mCallBack.onProgress(progress, total);
                }

                if (mBuilder != null) {
                    mBuilder.setContentTitle("正在下载：" + AppUpdateUtils.getAppName(DownloadService.this))
                            .setContentText(rate + "%")
                            .setProgress(100, rate, false)
                            .setWhen(System.currentTimeMillis());
                    Notification notification = mBuilder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(NOTIFY_ID, notification);
                }

                //重新赋值
                oldRate = rate;
            }


        }

        @Override
        public void onError(String error) {
            Toast.makeText(DownloadService.this, "更新新版本出错，" + error, Toast.LENGTH_SHORT).show();
            ILog.i(error);

            //App前台运行
            if (mCallBack != null) {
                mCallBack.onError(error);
            }
            try {
                mNotificationManager.cancel(NOTIFY_ID);
                close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void onResponse(File file) {
            if (mCallBack != null) {
                if (!mCallBack.onFinish(file)) {
                    return;
                }
            }

            if (AppUpdateUtils.isAppOnForeground(DownloadService.this) || mBuilder == null) {
                //App前台运行
                mNotificationManager.cancel(NOTIFY_ID);
                AppUpdateUtils.install(DownloadService.this, file);
            } else {
                //App后台运行
                //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                Intent installAppIntent = AppUpdateUtils.installBackground(DownloadService.this, file);
                PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, 0, installAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent)
                        .setContentTitle(AppUpdateUtils.getAppName(DownloadService.this))
                        .setContentText("下载完成，请点击安装")
                        .setProgress(0, 0, false)
//                        .setAutoCancel(true)
                        .setDefaults((Notification.DEFAULT_ALL));
                Notification notification = mBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(NOTIFY_ID, notification);
            }
            //下载完自杀
            close();
        }
    }
}

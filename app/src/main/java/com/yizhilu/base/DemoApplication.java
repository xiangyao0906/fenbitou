package com.yizhilu.base;


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.CacheIntercptor;
import com.yizhilu.utils.CrashHelper;
import com.yizhilu.utils.LogInterceptor;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzhoujay.richtext.RichText;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class DemoApplication extends MultiDexApplication {


    private static DemoApplication instance = null;

    private static Context currentActivity;
    private boolean isBackGround = false;

    public static String downloadUrl;   /// 更新apk下载地址
    public static String downloadContent;  /// 更新apk 内容描述
    public static String downloadPath = null;  /// 下载视频的地址
    public static String nikeName;// 用户昵称，用于直播间显示

    /**
     * 上下文
     */
    private Context mContext = null;
    /**
     * activity栈
     */
    private ActivityStack mActivityStack = null;

    public static void setCurrentActivity(Context context) {
        currentActivity = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        initOkHttpUtils();
//        initBLlive();
        RichText.initCacheDir(this);
        MobSDK.init(getApplicationContext());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        initLogger();
        ForegroundCallbacks.init(this);
        ForegroundCallbacks.get().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                isBackGround = false;

            }

            @Override
            public void onBecameBackground() {
                isBackGround = true;
            }
        });
        new Thread(new ThreadShow()).start();
        downloadPath = getDiskCacheDir(this, "videodownload");

    }


    public static DemoApplication getInstance() {
        return instance;
    }


    class ThreadShow implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 10);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ActivityStack getActivityStack() {
        if (mActivityStack == null) {
            synchronized (DemoApplication.class) {
                if (mActivityStack == null) {
                    mActivityStack = new ActivityStack();
                }
            }
        }
        return mActivityStack;
    }

    public Context getContext() {
        return mContext;
    }

    private void initOkHttpUtils() {
        File httpCacheDirectory = new File(mContext.getCacheDir(), "educache");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor(false))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new CacheIntercptor(mContext))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initLogger() {

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .tag("logok268")
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }

    /**
     * autour: Bishuang
     * date: 2017/8/31 14:04
     * 方法说明: 捕获接口异常
     */
    private void initException() {
        CrashHelper.install(new CrashHelper.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("CrashHelper", thread + "\n" + throwable.toString());
                            throwable.printStackTrace();
                            Toast.makeText(DemoApplication.this, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }

    //  --------------------------------保利直播-------------------------------------------
    private static final String appId = "em1edbz476";
    private static final String appSecret = "b79fcc13837a463faed71f0b3fcc737f";

    /**
     * autour: Bishuang
     * date: 2017/8/14 15:02
     * 方法说明: 初始化保利直播的方法
     */
    public void initBLlive() {
        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }


    private int userId;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                userId = (int) SharedPreferencesUtils.getParam(mContext, "userId", -1);
                String memTime = (String) SharedPreferencesUtils.getParam(mContext, "memTime", "");
                if (userId != -1) {
                    if (!TextUtils.isEmpty(memTime) && !isBackGround) {
                        getCheckUserIsLogin(memTime, userId);
                    }
                }
            }
        }

    };

    /**
     * 检查用户是否在其他地方登录账号
     *
     * @param cookieTime
     * @param userId
     */
    public void getCheckUserIsLogin(String cookieTime, int userId) {

        OkHttpUtils.get().addParams("cookieTime", cookieTime)
                .addParams("userId", String.valueOf(userId))
                .url(Address.CHECK_USERISLOGIN).build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if (!TextUtils.isEmpty(response.toString())) {
                            boolean success = response.isSuccess();
                            if (!success) {
                                ((BaseActivity) mActivityStack.getLastActivity()).showQuitDiaLog();
                            }
                        }
                    }
                });
    }


    /**
     * 获取应用路径，下载视频使用，当卸载应用时，同时视频也删除
     *
     * @param context
     * @param path
     * @return
     */
    public static String getDiskCacheDir(Context context, String path) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(path).getAbsolutePath();
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
        }
        return cachePath;
    }


}

package com.fenbitou.base;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.screencast.PolyvScreencastHelper;
import com.fenbitou.bl.util.PolyvStorageUtils;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.CacheIntercptor;
import com.fenbitou.utils.CrashHelper;
import com.fenbitou.utils.LogInterceptor;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzhoujay.richtext.RichText;

import java.io.File;
import java.util.ArrayList;
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


        initPolyvCilent();
        initScreencast();


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

    public void initScreencast() {
        //TODO appId和appSecret需与包名绑定，获取方式请咨询Polyv技术支持
        PolyvScreencastHelper.init("10747", "34fa2201e4e7441635ca4fa97fd4b21e");//该appId，appSecret仅能在demo中使用
        //初始化单例
        PolyvScreencastHelper.getInstance(this);
    }


    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /** 加密秘钥 */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /** 加密向量 */
    private String iv = "2u9gDPKdX6GyQJKU";

    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        setDownloadDir();
        // 设置下载队列总数，多少个视频能同时下载。(默认是1，设置负数和0是没有限制)
        PolyvDownloaderManager.setDownloadQueueCount(1);
    }


    /**
     * 设置下载视频目录
     */
    private void setDownloadDir() {
        String rootDownloadDirName = "polyvdownload";
        ArrayList<File> externalFilesDirs = PolyvStorageUtils.getExternalFilesDirs(getApplicationContext());
        if (externalFilesDirs.size() == 0) {
            // TODO 没有可用的存储设备,后续不能使用视频缓存功能
            Log.e("xiangyao", "没有可用的存储设备,后续不能使用视频缓存功能");
            return;
        }

        //SD卡会有SD卡接触不良，SD卡坏了，SD卡的状态错误的问题。
        //我们在开发中也遇到了SD卡没有权限写入的问题，但是我们确定APP是有赋予android.permission.WRITE_EXTERNAL_STORAGE权限的。
        //有些是系统问题，有些是SD卡本身的问题，这些问题需要通过重新拔插SD卡或者更新SD卡来解决。所以如果想要保存下载视频至SD卡请了解这些情况。
        File downloadDir = new File(externalFilesDirs.get(0), rootDownloadDirName);
        PolyvSDKClient.getInstance().setDownloadDir(downloadDir);

        //兼容旧下载视频目录，如果新接入SDK，无需使用以下代码
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                //现在PolyvDevMountInfo.getInstance().getExternalSDCardPath()默认返回的目录路径就是/storage/sdcard1/Android/data/包名/。
                //跟PolyvDevMountInfo.getInstance().init(Context context, final OnLoadCallback callback)接口有区别，请保持同步修改。
                ArrayList<File> subDirList = new ArrayList<>();
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("polyvdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        saveDir.mkdirs();//创建下载目录
                    }

                    //设置下载存储目录
//					PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//					return;
                    subDirList.add(saveDir);
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();//创建下载目录
                }

                //设置下载存储目录
//				PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                subDirList.add(saveDir);
                PolyvSDKClient.getInstance().setSubDirList(subDirList);
            }
        }, true);
    }

    private class LoadConfigTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String config = PolyvSDKUtil.getUrl2String("http://demo.polyv.net/demo/appkey.php");
            if (TextUtils.isEmpty(config)) {
                try {
                    throw new Exception("没有取到数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return config;
        }

        @Override
        protected void onPostExecute(String config) {
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setConfig(config, aeskey, iv);
        }
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

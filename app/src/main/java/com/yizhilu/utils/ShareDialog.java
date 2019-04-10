package com.yizhilu.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareDialog extends Dialog
        implements View.OnClickListener, OnItemClickListener, PlatformActionListener, Callback {

    private Context context;
    private String texts[] = null;
    private int images[] = null;
    private GridView gridView;
    private Platform.ShareParams sp;
    private ImageView cancel; // 取消
    private ArrayList<HashMap<String, Object>> lstImageItem;
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private EntityCourse course; // 课程的实体
    private EntityPublic topicCourse;
    private boolean isCourse, isInformation, isAbout, isTopic; // 课程,资讯,关于我们
    private String code = null;
    private String userId, userIdMD5;

    public ShareDialog(Context context) {
        super(context);
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        // 初始化控件的方法
        initView();
        // 添加点击事件的方法
        addOnClick();
    }

    public void shareInfo(EntityCourse course, boolean isCourse, boolean isInformation, boolean isAbout) {
        this.course = course;
        this.isCourse = isCourse;
        this.isInformation = isInformation;
        this.isAbout = isAbout;
    }

    public void shareInfo(EntityPublic course, boolean isCourse, boolean isInformation, boolean isAbout,
                          boolean isTopic) {
        this.topicCourse = course;
        this.isCourse = isCourse;
        this.isInformation = isInformation;
        this.isAbout = isAbout;
        this.isTopic = isTopic;
    }

    /**
     * @author bin 修改人: 时间:2016-2-19 下午5:38:00 方法说明:初始化控件的方法
     */
    public void initView() {

        sp = new Platform.ShareParams();
        userId = String.valueOf(SharedPreferencesUtils.getParam(context, "userId", -1));  //得到用户Id
//		userId = String.valueOf(context.getSharedPreferences("userId", Context.MODE_PRIVATE).getInt("userId",-1));
        Log.i("ceshi", userId + "-------------userId");
        texts = new String[]{"QQ好友", "QQ空间", "微信好友", "微信朋友圈"};
        images = new int[]{R.drawable.share_qq, R.drawable.share_qqzone, R.drawable.share_weixin,
                R.drawable.share_weixinp};
        gridView = (GridView) findViewById(R.id.gridview);
        cancel = (ImageView) findViewById(R.id.cancel); // 取消
        lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemText", texts[i]);
            map.put("itemImage", images[i]);
            lstImageItem.add(map);
        }
        SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem, R.layout.item_share,
                new String[]{"itemImage", "itemText"}, new int[]{R.id.itemImage, R.id.itemText});
        userIdMD5 = StringUtil.encrypt32(userId);
        gridView.setAdapter(saImageItems);
    }


    /**
     * @author bin 修改人: 时间:2016-2-19 下午5:38:11 方法说明:添加点击事件的方法
     */
    public void addOnClick() {
        cancel.setOnClickListener(this); // 取消
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel: // 取消
                this.cancel();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: // qq好友
                if (ShareSDK.getPlatform(QQ.NAME).isClientValid()) {
                    if (isCourse) {
                        code = StringUtil.replaceHtml(course.getContext());
                        sp.setTitle(course.getName());
                        sp.setTitleUrl(Address.COURSE_SHARE + course.getId() + ".json");
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
                    } else if (isInformation) {
                        code = StringUtil.replaceHtml(course.getDescription());
                        sp.setTitle(course.getTitle());
                        sp.setTitleUrl(Address.INFORMATION_SHARE + course.getId() + ".json");
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
                    } else if (isAbout) {
                        sp.setTitle(context.getResources().getString(R.string.apk_name) + "软件");
                        Log.i("lala", Address.MOBILE_INDEX + "?share_from=" + userIdMD5 + "_" + userId + "-------分享");
//						sp.setTitleUrl(Address.MOBILE_INDEX+"?share_from="+userIdMD5+"_"+userId);
                        sp.setTitleUrl(Address.MOBILE_INDEX);
                        sp.setText(context.getResources().getString(R.string.apk_name_share));
//						sp.setImageUrl("http://www.268xue.com/app/images/appIcon.png");
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    } else if (isTopic) {
                        code = StringUtil.replaceHtml(topicCourse.getContent());
                        sp.setTitle(topicCourse.getTitle());
//                        sp.setTitleUrl(Address.MOBILE_INDEX); // 请修改分享地址
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    }
                    Platform platform = ShareSDK.getPlatform(QQ.NAME);
                    platform.setPlatformActionListener(this);
                    platform.share(sp);
                    ShareDialog.this.cancel();
                } else {
                    Toast.makeText(context, "请安装QQ客户端", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1: // qq空间
                if (ShareSDK.getPlatform(QQ.NAME).isClientValid()) {
                    if (isCourse) {
                        code = StringUtil.replaceHtml(course.getContext());
                        sp.setTitle(course.getName());
                        sp.setTitleUrl(Address.COURSE_SHARE + course.getId() + ".json");
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
                    } else if (isInformation) {
                        code = StringUtil.replaceHtml(course.getDescription());
                        sp.setTitle(course.getTitle());
                        sp.setTitleUrl(Address.INFORMATION_SHARE + course.getId() + ".json");
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
                    } else if (isAbout) {
                        sp.setTitle(context.getResources().getString(R.string.apk_name) + "软件");
//						sp.setTitleUrl(Address.MOBILE_INDEX+"?share_from="+userIdMD5+"_"+userId);
                        sp.setTitleUrl(Address.MOBILE_INDEX);
                        sp.setText(context.getResources().getString(R.string.apk_name_share));
//						sp.setImageUrl("http://www.268xue.com/app/images/appIcon.png");
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));

                    } else if (isTopic) {
                        code = StringUtil.replaceHtml(topicCourse.getContent());
                        sp.setTitle(topicCourse.getTitle());
//                        sp.setTitleUrl(Address.MOBILE_INDEX); // 请修改分享地址
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    }
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(this);
                    qzone.share(sp);
                    ShareDialog.this.cancel();
                } else {
                    Toast.makeText(context, "请安装QQ客户端", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2: // 微信
                if (ShareSDK.getPlatform(Wechat.NAME).isClientValid()) {
                    sp.setShareType(Platform.SHARE_WEBPAGE);
                    if (isCourse) {
                        code = StringUtil.replaceHtml(course.getContext());
                        sp.setTitle(course.getName());
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setUrl(Address.COURSE_SHARE + course.getId() + ".json");
                        sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
                    } else if (isInformation) {
                        code = StringUtil.replaceHtml(course.getDescription());
                        sp.setTitle(course.getTitle());
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setUrl(Address.INFORMATION_SHARE + course.getId() + ".json");
                        sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
                    } else if (isAbout) {
                        sp.setTitle(context.getResources().getString(R.string.apk_name));
                        sp.setText(context.getResources().getString(R.string.apk_name_share));
//						sp.setUrl(Address.MOBILE_INDEX+"?share_from="+userIdMD5+"_"+userId);
                        sp.setUrl(Address.MOBILE_INDEX);
//						sp.setImageUrl("http://www.268xue.com/app/images/appIcon.png");
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    } else if (isTopic) {
                        code = StringUtil.replaceHtml(topicCourse.getContent());
                        sp.setTitle(topicCourse.getTitle());
//                        sp.setUrl(Address.MOBILE_INDEX); // 请修改分享地址
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    }
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(this);
                    wechat.share(sp);
                    ShareDialog.this.cancel();
                } else {
                    IToast.show(context, "请安装微信客户端");
                }
                break;
            case 3: // 微信朋友圈
                if (ShareSDK.getPlatform(Wechat.NAME).isClientValid()) {
                    sp.setShareType(Platform.SHARE_WEBPAGE);
                    if (isCourse) {
                        code = StringUtil.replaceHtml(course.getContext());
                        sp.setTitle(course.getName());
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setUrl(Address.COURSE_SHARE + course.getId() + ".json");
                        sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
                    } else if (isInformation) {
                        code = StringUtil.replaceHtml(course.getDescription());
                        sp.setTitle(course.getTitle());
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setUrl(Address.INFORMATION_SHARE + course.getId() + ".json");
                        sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
                    } else if (isAbout) {
                        sp.setTitle(context.getResources().getString(R.string.apk_name));
                        sp.setText(context.getResources().getString(R.string.apk_name_share));
//						sp.setUrl(Address.MOBILE_INDEX+"?share_from="+userIdMD5+"_"+userId);
                        sp.setUrl(Address.MOBILE_INDEX);
//						sp.setImageUrl("http://www.268xue.com/app/images/appIcon.png");
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    } else if (isTopic) {
                        code = StringUtil.replaceHtml(topicCourse.getContent());
                        sp.setTitle(topicCourse.getTitle());
//                        sp.setUrl(Address.MOBILE_INDEX); // 请修改分享地址
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    }
                    Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechat.setPlatformActionListener(this);
                    wechat.share(sp);
                    ShareDialog.this.cancel();
                } else {
                    IToast.show(context, "请安装微信客户端");
                }
                break;
            case 4: // 新浪微博
                if (ShareSDK.getPlatform(SinaWeibo.NAME).isClientValid()) {
                    if (isCourse) {
                        code = StringUtil.replaceHtml(course.getContext());
                        sp.setTitle(course.getName());
                        sp.setTitleUrl(Address.COURSE_SHARE + course.getId() + ".json");
                        sp.setText("123");
                        sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());

                        Log.i("lala", course.getName() + "------" + Address.COURSE_SHARE + course.getId() + ".json" + "-----" + course.getMobileLogo());

                    } else if (isInformation) {
                        code = StringUtil.replaceHtml(course.getDescription());
                        sp.setTitle(course.getTitle());
                        sp.setTitleUrl(Address.INFORMATION_SHARE + course.getId() + ".json");
                        sp.setText(replaceTagHTML(code).trim());
                        sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
                    } else if (isAbout) {
                        sp.setTitle(context.getResources().getString(R.string.apk_name) + "软件");
//						sp.setTitleUrl(Address.MOBILE_INDEX+"?share_from="+userIdMD5+"_"+userId);
                        sp.setTitleUrl(Address.MOBILE_INDEX);
                        sp.setText(context.getResources().getString(R.string.apk_name_share));
//						sp.setImageUrl("http://www.268xue.com/app/images/appIcon.png");
                        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
                    } else if (isTopic) {
                    }
                    Platform sign = ShareSDK.getPlatform(SinaWeibo.NAME);
                    sign.setPlatformActionListener(this);
                    sign.share(sp);
                    ShareDialog.this.cancel();
                } else {
                    IToast.show(context, "请安装新浪微博客户端");
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = arg1;
        msg.obj = arg0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = arg1;
        msg.obj = arg0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = arg1;
        msg.obj = arg2;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }
            break;
            case 2: {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    public static String replaceTagHTML(String src) {
        String regex = "\\<(.+?)\\>";
        if (!TextUtils.isEmpty(src)) {
            return src.replaceAll(regex, "");
        }
        return "";
    }

}

package com.yizhilu.eduapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 个人信息类
 */
public class PersonalInformationActivity extends BaseActivity {

    @BindView(R.id.personal_image)
    ImageView userHeadImage;
    @BindView(R.id.user_nickname)
    TextView userNickname;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_qianMing)
    TextView userQianMing;
    @BindView(R.id.nickNameLayout)
    LinearLayout nickNameLayout;
    @BindView(R.id.name_layout)
    LinearLayout nameLayout;
    @BindView(R.id.sex_layout)
    LinearLayout sexLayout;
    @BindView(R.id.jianjie_layout)
    LinearLayout jianjieLayout;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.change_password)
    LinearLayout changePassword;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.remove_binding_layout)
    LinearLayout removeBindingLayout;
    @BindView(R.id.binding_phone_text)
    TextView bindingPhoneText;
    @BindView(R.id.binding_phone_layout)
    LinearLayout bindingPhoneLayout;
    @BindView(R.id.binding_email_text)
    TextView bindingEmailText;
    @BindView(R.id.binding_email_layout)
    LinearLayout bindingEmailLayout;
    private int userId = -1;
    private EntityPublic userExpandDto; // 用户信息的实体
    private EntityPublic binDingEntity;  //绑定的实体
    private Dialog dialog; // 选择头像时弹出

    @Override
    protected int initContentView() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void initComponent() {
        titleText.setText("个人信息");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        getUserMessage();
        getUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        ILog.i(Address.MY_MESSAGE + map + "----------获取用户信息11111");
        showLoading(PersonalInformationActivity.this);
        OkHttpUtils.post().params(map).url(Address.MY_MESSAGE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                cancelLoading();
                                userExpandDto = response.getEntity().getUserExpandDto();
                                GlideUtil.loadCircleHeadImage(PersonalInformationActivity.this,
                                        Address.IMAGE_NET + userExpandDto.getAvatar(),
                                        userHeadImage);
                                userNickname.setText(userExpandDto.getShowname());
                                userName.setText(userExpandDto.getRealname());
                                if (userExpandDto.getGender() == 0) {
                                    userSex.setText("男");
                                } else {
                                    userSex.setText("女");
                                }
                                userQianMing.setText(userExpandDto.getUserInfo());
                            } else {
                                IToast.show(PersonalInformationActivity.this, response.getMessage());
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }
                    }
                });
    }

    public void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        ILog.i(Address.QUERYUSERINFO + map + "----------UserInfo");
        OkHttpUtils.post().params(map).url(Address.QUERYUSERINFO).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                binDingEntity = response.getEntity();
                                String mobile = binDingEntity.getMobile();
                                String email = binDingEntity.getEmail();
                                if (TextUtils.isEmpty(mobile)) {
                                    bindingPhoneText.setText("未绑定");
                                    bindingPhoneText.setTextColor(getResources().getColor(R.color.color_7f));
                                } else {
                                    bindingPhoneText.setText(mobile);
                                    bindingPhoneText.setTextColor(getResources().getColor(R.color.color_33));
                                }
                                if (TextUtils.isEmpty(email)) {
                                    bindingEmailText.setText("未绑定");
                                    bindingEmailText.setTextColor(getResources().getColor(R.color.color_7f));
                                } else {
                                    bindingEmailText.setText(email);
                                    bindingEmailText.setTextColor(getResources().getColor(R.color.color_33));
                                }
                            } else {
                                IToast.show(PersonalInformationActivity.this, response.getMessage());
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.nickNameLayout, R.id.binding_phone_layout, R.id.binding_email_layout,
            R.id.name_layout, R.id.sex_layout, R.id.jianjie_layout, R.id.back_layout,
            R.id.change_password, R.id.remove_binding_layout})
    public void onClick(View v) {
        if (userExpandDto == null) {
            IToast.show(PersonalInformationActivity.this, "没有获取到用户信息");
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.nickNameLayout:
                intent.setClass(PersonalInformationActivity.this,
                        AmendUserMessageActivity.class);
                intent.putExtra("title", "修改昵称");
                intent.putExtra("message", userExpandDto.getShowname());
                startActivity(intent);
                break;
            case R.id.name_layout:
                intent.setClass(PersonalInformationActivity.this,
                        AmendUserMessageActivity.class);
                intent.putExtra("title", "修改姓名");
                intent.putExtra("message", userExpandDto.getRealname());
                startActivity(intent);
                break;
            case R.id.sex_layout:
                intent.setClass(PersonalInformationActivity.this,
                        AmendUserMessageActivity.class);
                intent.putExtra("title", "选择性别");
                intent.putExtra("sex", userExpandDto.getGender());
                startActivity(intent);
                break;
            case R.id.jianjie_layout:
                intent.setClass(PersonalInformationActivity.this,
                        AmendUserMessageActivity.class);
                intent.putExtra("title", "个性签名");
                intent.putExtra("message", userExpandDto.getUserInfo());
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent.setClass(PersonalInformationActivity.this,
                        ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.remove_binding_layout://第三方绑定
                intent.setClass(PersonalInformationActivity.this,
                        RemoveBindingActivity.class);
                intent.putExtra("EntityPublic", userExpandDto);
                startActivity(intent);
                break;
            case R.id.binding_phone_layout://绑定手机
                String mobile = binDingEntity.getMobile();
                if (TextUtils.isEmpty(mobile)) {
                    //第一次绑定
                    intent.setClass(PersonalInformationActivity.this, PhoneOrEmailActivity.class);
                } else {
                    intent.setClass(PersonalInformationActivity.this, BindingPhoneEmailActivity.class);
                }
                intent.putExtra("type", "phone");
                intent.putExtra("entity", binDingEntity);
                startActivity(intent);
                break;
            case R.id.binding_email_layout://绑定邮箱
                String email = binDingEntity.getEmail();
                if (TextUtils.isEmpty(email)) {
                    intent.setClass(PersonalInformationActivity.this, PhoneOrEmailActivity.class);
                } else {
                    intent.setClass(PersonalInformationActivity.this, BindingPhoneEmailActivity.class);
                }
                intent.putExtra("type", "email");
                intent.putExtra("entity", binDingEntity);
                startActivity(intent);
                break;
            case R.id.back_layout:
                this.finish();
                break;

        }
    }

    @OnClick(R.id.personal_image)
    public void changeUserHead() {
        if (userExpandDto == null) return;
        if (dialog == null) {
            dialog = new Dialog(PersonalInformationActivity.this, R.style.custom_dialog);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setContentView(R.layout.dialog_change_head);
            Button camera_btn = (Button) dialog.findViewById(R.id.camera_btn);
            Button picture_btn = (Button) dialog.findViewById(R.id.picture_btn);
            camera_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 0);
                    dialog.dismiss();
                }
            });
            picture_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    /**
     * 选择完头像调用的方法
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                String url = getPath(data);
                // 上传头像的方法
                uploadIcon(url);
            } else {
                if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                    return;

                String name = DateFormat.format("yyyy-MM-dd hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                // 获取相机返回的数据，并转换为图片格式
                Bitmap bitmap = (Bitmap) bundle.get("data");
                FileOutputStream fos = null;
                File file = new File("/sdcard/pintu");
                String filename = file.getPath() + "/" + name;
                file.mkdirs();
                try {
                    fos = new FileOutputStream(filename);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
                    uploadIcon(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @author bin 修改人: 时间:2015-10-28 下午7:39:20 方法说明:返回选择相册图片url
     */
    public String getPath(Intent data) {
        Uri selectedImage = getUri(data);
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(selectedImage, filePathColumns,
                null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        return picturePath;
    }

    public Uri getUri(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /**
     * 图片上传
     */
    public void uploadIcon(String path) {
        File file = new File(path);
        ILog.i(file.length() + "_________________");
        try {
            OkHttpUtils.post()
                    .url(Address.UP_IMAGE_NET)
                    .addFile("fileupload", "2017-07-26 100957.jpg", file)
                    .addParams("base", "mavendemo")
                    .addParams("param", "appavatar")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            modifyIcon(response);
                            ILog.i(response + "_______2__________");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author bin 修改人: 时间:2015-9-30 上午9:25:05 方法说明:修改头像的方法
     */
    private void modifyIcon(String path) {
        OkHttpUtils.get().url(Address.UPDATE_HEAD)
                .addParams("userId", String.valueOf(userId))
                .addParams("avatar", path)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if (response.isSuccess()) {
                            IToast.show(PersonalInformationActivity.this, response.getMessage());
                            getUserMessage();
                        }
                    }
                });
    }

}

package com.fenbitou.BLDowload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.base.DemoApplication;
import com.fenbitou.bl.activity.PolyvDownloadActivity;
import com.fenbitou.bl.bean.PolyvDownloadInfo;
import com.fenbitou.bl.database.PolyvDownloadSQLiteHelper;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.widget.NoScrollExpandableListView;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/8/29.
 * 课程下载的类
 */

public class DownloadCourseActivity extends BaseActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.download_expandablelist)
    NoScrollExpandableListView mListView;
    private PublicEntity publicEntity;  //课程详情的信息
    private int parentId;
    private int userId, courseId, kpointId; //用户名 课程id 节点id
    private boolean isok;  //是否购买
    private String courseName, courseImg, kpointName; // 课程名字 课程图片,节点名
    private List<EntityPublic> courseList; // 课程列表数据
    private DownloadListAdapter adapter;
    private EntityPublic entitychild, entitygroup; //子数据 组数据
    private String selectType; //未下载前视频类型
    private PolyvDownloadSQLiteHelper downloadSQLiteHelper;

    @Override
    protected int initContentView() {
        return R.layout.activity_download_course;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        courseList = new ArrayList<>();
        titleText.setText(R.string.down_list);
    }

    /**
     * autour: Bishuang
     * date: 2017/8/29 14:33
     * 方法说明: 获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        publicEntity = (PublicEntity) intent.getSerializableExtra("publicEntity");
        parentId = intent.getIntExtra("listId", 0);
        courseId = intent.getIntExtra("courseId", 0);
        isok = publicEntity.getEntity().isok();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        this.downloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(this);
    }

    @Override
    protected void initData() {

        courseName = publicEntity.getEntity().getCourse().getName();
        courseImg = publicEntity.getEntity().getCourse().getMobileLogo();
        //获取目录的方法
        getCourseDetails(courseId, userId, parentId);
    }

    @Override
    protected void addListener() {
        mListView.setOnChildClickListener(this);
    }

    @OnClick({R.id.back_layout,R.id.downLoadLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout://返回
                finish();
                break;
            case R.id.downLoadLayout://返回

                startActivity(new Intent(this, PolyvDownloadActivity.class));
                break;
        }
    }

    /**
     * autour: Bishuang
     * date: 2017/8/29 14:43
     * 方法说明: 获取课程详情的方法
     */
    private void getCourseDetails(int courseId, int userId, final int id) {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", String.valueOf(courseId));
        map.put("userId", String.valueOf(userId));
        map.put("currentCourseId", String.valueOf(id));
        showLoading(DownloadCourseActivity.this);
        Log.i("shuang", Address.COURSE_DETAILS + "?" + map + "-------获取课程详情的方法");
        OkHttpUtils.post().params(map).url(Address.COURSE_DETAILS).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(DownloadCourseActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        if (response.isSuccess()) {
                            cancelLoading();
                            List<EntityPublic> courseKpoints = response.getEntity().getCourseKpoints();
                            Log.i("shuang", courseKpoints.size() + "---------------lise.size()");
                            if (courseKpoints.size() > 0) {
                                for (int i = 0; i < courseKpoints.size(); i++) {
                                    courseList.add(courseKpoints.get(i));
                                }
                                adapter = new DownloadListAdapter(DownloadCourseActivity.this, courseList);
                                mListView.setAdapter(adapter);
                                mListView.expandGroup(0);
                            }

                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    private String fileType; //VIDEO 视频 AUDIO 音频
    private Dialog dialog;//下载的dialog

    /*
     * 子的点击事件
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        entitygroup = courseList.get(groupPosition);
        entitychild = entitygroup.getChildKpoints().get(childPosition);
        kpointId = entitychild.getId();
        kpointName = entitychild.getName();
        selectType = entitychild.getFileType();
        ImageView itemTypeImg = (ImageView) v.findViewById(R.id.download_playType);
        TextView itemText = (TextView) v.findViewById(R.id.download_child_text);
//        if (selectType.equals("VIDEO")) {
//            fileType = "VIDEO";
//            showDiaLog();
//        } else {
//            fileType = "AUDIO";
//            downLoadVideo();
//        }
//        modifySytle(itemTypeImg, itemText);
//        PolyvDownloadInfo{vid='c538856dde2600e0096215c16592d4d3_c', duration='492.0', filesize=15858096, bitrate=1, title='1', percent=0, total=0, fileType=0}
        final PolyvDownloadInfo downloadInfo = new PolyvDownloadInfo("c538856dde2600e0096215c16592d4d3_c","492.0",15858096,1,"1");
        downloadInfo.setFileType(PolyvDownloader.FILE_VIDEO);
        if (!downloadSQLiteHelper.isAdd(downloadInfo)) {
            downloadSQLiteHelper.insert(downloadInfo);
            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(downloadInfo.getVid(), downloadInfo.getBitrate(), downloadInfo.getFileType());
            downloader.setPolyvDownloadProressListener(new MyDownloadListener(downloadInfo));
            downloader.start();
            Toast.makeText(this,"已加入下载列表",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"视频已经下载",Toast.LENGTH_SHORT).show();

        }



        return false;
    }

    public void modifySytle(ImageView typeImg, TextView text) {
        text.setTextColor(getResources().getColor(R.color.Blue));
        if (selectType.equals("AUDIO")) {
            typeImg.setBackgroundResource(R.drawable.sign_audio_yes);
        } else {
            typeImg.setBackgroundResource(R.drawable.sign_video_yes);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoading();
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        return false;
    }

    // 是否要下载
    public void showDiaLog() {
        View view = LayoutInflater.from(DownloadCourseActivity.this).inflate(
                R.layout.dialog_96down, null);
        WindowManager manager = (WindowManager) DownloadCourseActivity.this.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(DownloadCourseActivity.this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        TextView video = (TextView) view.findViewById(R.id.dialog_video);
        TextView audio = (TextView) view.findViewById(R.id.dialog_audio);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "VIDEO";
                downLoadVideo();
                dialog.cancel();
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "AUDIO";
                downLoadVideo();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    private void downLoadVideo() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("kpointId", String.valueOf(kpointId));
        OkHttpUtils.post().params(map).url(Address.DOWNLOAD_CHECK).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        if (response.isSuccess()) { // 可以下载
                            EntityPublic entityPublic = response.getEntity();
                            String videoUrl = entityPublic.getVideoUrl(); // 视频码
                            if (TextUtils.isEmpty(videoUrl)) {
                                ConstantUtils.showMsg(DownloadCourseActivity.this, "无视频路径");
                                return;
                            }

                        } else {
                            if (isok) {
                                ConstantUtils.showMsg(DownloadCourseActivity.this, "该视频无法下载");
                            } else {
                                if (userId == 0) {
                                    ConstantUtils.showMsg(DownloadCourseActivity.this, "请登录购买后下载");
                                } else {
                                    ConstantUtils.showMsg(DownloadCourseActivity.this, "请购买后下载");
                                }
                            }
                        }
                    } catch (Exception e) {
                        Logger.i(e.getMessage());
                    }
                }
            }
        });
    }

    private String FILETYPE = "fileType";
    private String VIDEOURL = "videoUrl";
    private String COURSENAME = "courseName";
    private String COURSEIMG = "courseImg";

    private static class MyDownloadListener implements IPolyvDownloaderProgressListener {
        private PolyvDownloadInfo downloadInfo;
        private long total;

        public MyDownloadListener(PolyvDownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void onDownload(long current, long total) {
            this.total = total;
        }

        @Override
        public void onDownloadSuccess() {
            if (total == 0)
                total = 1;
            PolyvDownloadSQLiteHelper.getInstance(DemoApplication.getInstance()).update(downloadInfo, total, total);
        }

        @Override
        public void onDownloadFail(@NonNull PolyvDownloaderErrorReason errorReason) {
        }
    }
}

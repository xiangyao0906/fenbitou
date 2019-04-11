package com.fenbitou.bl.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvBitRate;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.marquee.PolyvMarqueeItem;
import com.easefun.polyvsdk.marquee.PolyvMarqueeView;
import com.easefun.polyvsdk.screencast.PolyvScreencastHelper;
import com.easefun.polyvsdk.srt.PolyvSRTItemVO;
import com.easefun.polyvsdk.sub.vlms.entity.PolyvCoursesInfo;
import com.easefun.polyvsdk.video.PolyvMediaInfoType;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvSeekType;
import com.easefun.polyvsdk.video.PolyvVideoUtil;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.auxiliary.PolyvAuxiliaryVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementCountDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementEventListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementOutListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnChangeModeListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnCompletionListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureDoubleClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeLeftListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeRightListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnInfoListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnPlayPauseListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreloadPlayListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnQuestionAnswerTipsListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnQuestionOutListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnTeaserCountDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnTeaserOutListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoSRTListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoSRTPreparedListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoTimeoutListener;
import com.easefun.polyvsdk.vo.PolyvADMatterVO;
import com.easefun.polyvsdk.vo.PolyvQuestionVO;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.fenbitou.bl.fragment.PolyvPlayerDanmuFragment;
import com.fenbitou.bl.fragment.PolyvPlayerTabFragment;
import com.fenbitou.bl.fragment.PolyvPlayerTopFragment;
import com.fenbitou.bl.fragment.PolyvPlayerViewPagerFragment;
import com.fenbitou.bl.player.PolyvPlayerAnswerView;
import com.fenbitou.bl.player.PolyvPlayerAudioCoverView;
import com.fenbitou.bl.player.PolyvPlayerAuditionView;
import com.fenbitou.bl.player.PolyvPlayerAuxiliaryView;
import com.fenbitou.bl.player.PolyvPlayerLightView;
import com.fenbitou.bl.player.PolyvPlayerMediaController;
import com.fenbitou.bl.player.PolyvPlayerPlayErrorView;
import com.fenbitou.bl.player.PolyvPlayerPlayRouteView;
import com.fenbitou.bl.player.PolyvPlayerPreviewView;
import com.fenbitou.bl.player.PolyvPlayerProgressView;
import com.fenbitou.bl.player.PolyvPlayerVolumeView;
import com.fenbitou.bl.util.PolyvNetworkDetection;
import com.fenbitou.bl.util.PolyvScreenUtils;
import com.fenbitou.bl.view.PolyvScreencastSearchLayout;
import com.fenbitou.bl.view.PolyvScreencastStatusLayout;
import com.fenbitou.wantongzaixian.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * 在线视频
 *
 *
 * 是否现实左侧音视频切换按钮
 *
 * 根据videoview.video 的 aac 字段是否为空
 *
 * */
public class PolyvPlayerActivity extends FragmentActivity {
    private static final String TAG = PolyvPlayerActivity.class.getSimpleName();
    private PolyvPlayerTopFragment topFragment;
    private PolyvPlayerTabFragment tabFragment;
    private PolyvPlayerViewPagerFragment viewPagerFragment;
    private PolyvPlayerDanmuFragment danmuFragment;
    private ImageView iv_vlms_cover;
    //投屏相关
    private PolyvScreencastHelper screencastHelper;
    private PolyvScreencastStatusLayout fl_screencast_status;
    private PolyvScreencastSearchLayout fl_screencast_search, fl_screencast_search_land;
    private ImageView iv_screencast_search, iv_screencast_search_land;
    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView videoView = null;
    /**
     * 跑马灯控件
     */
    private PolyvMarqueeView marqueeView = null;
    private PolyvMarqueeItem marqueeItem = null;
    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaController mediaController = null;
    /**
     * 底部字幕文本视图
     */
    private TextView srtTextView = null;
    /**
     * 顶部字幕文本试图
     */
    private TextView topSrtTextView = null;
    /**
     * 普通问答界面
     */
    private PolyvPlayerAnswerView questionView = null;
    /**
     * 语音问答界面
     */
    private PolyvPlayerAuditionView auditionView = null;
    /**
     * 用于播放广告片头的播放器
     */
    private PolyvAuxiliaryVideoView auxiliaryVideoView = null;
    /**
     * 视频广告，视频片头加载缓冲视图
     */
    private ProgressBar auxiliaryLoadingProgress = null;
    /**
     * 图片广告界面
     */
    private PolyvPlayerAuxiliaryView auxiliaryView = null;
    /**
     * 广告倒计时
     */
    private TextView advertCountDown = null;
    /**
     * 缩略图界面
     */
    private PolyvPlayerPreviewView firstStartView = null;
    /**
     * 手势出现的亮度界面
     */
    private PolyvPlayerLightView lightView = null;
    /**
     * 手势出现的音量界面
     */
    private PolyvPlayerVolumeView volumeView = null;
    /**
     * 手势出现的进度界面
     */
    private PolyvPlayerProgressView progressView = null;
    /**
     * 音频模式下的封面
     */
    private PolyvPlayerAudioCoverView coverView = null;
    /**
     * mp3源文件播放时的封面图
     */
    private PolyvPlayerAudioCoverView audioSourceCoverView = null;
    /**
     * 视频加载缓冲视图
     */
    private ProgressBar loadingProgress = null;
    /**
     * 视频播放错误提示界面
     */
    private PolyvPlayerPlayErrorView playErrorView = null;
    /**
     * 线路切换界面
     */
    private PolyvPlayerPlayRouteView playRouteView = null;

    private int fastForwardPos = 0;
    private boolean isPlay = false;

    private boolean isBackgroundPlay = false;

    private String vid;
    private int bitrate;
    private boolean isMustFromLocal;
    private int fileType;

    private PolyvNetworkDetection networkDetection;
    private LinearLayout flowPlayLayout;
    private TextView flowPlayButton, cancelFlowPlayButton;
    private View.OnClickListener flowButtonOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            savedInstanceState.putParcelable("android:support:fragments", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_activity_player);
        addFragment();
        findIdAndNew();
        initView();
        initPlayErrorView();
        initRouteView();
        initScreencast();

        PolyvScreenUtils.generateHeight16_9(this);
        int playModeCode = getIntent().getIntExtra("playMode", PlayMode.portrait.getCode());
        PlayMode playMode = PlayMode.getPlayMode(playModeCode);
        if (playMode == null)
            playMode = PlayMode.portrait;
        vid = getIntent().getStringExtra("value");
        bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());
        boolean startNow = getIntent().getBooleanExtra("startNow", false);
        isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);
        fileType = getIntent().getIntExtra("fileType", PolyvDownloader.FILE_VIDEO);

        switch (playMode) {
            case landScape:
                mediaController.changeToLandscape();
                break;
            case portrait:
                mediaController.changeToPortrait();
                break;
        }

        initNetworkDetection(fileType);

        play(vid, bitrate, startNow, isMustFromLocal);
    }

    private void initNetworkDetection(int fileType) {
        networkDetection = new PolyvNetworkDetection(this);
        mediaController.setPolyvNetworkDetetion(networkDetection, flowPlayLayout, flowPlayButton, cancelFlowPlayButton, fileType);//传给mediaController，在切换清晰度时验证
        networkDetection.setOnNetworkChangedListener(new PolyvNetworkDetection.IOnNetworkChangedListener() {
            @Override
            public void onChanged(int networkType) {
                if (videoView.isLocalPlay())
                    return;
                if (networkDetection.isMobileType()) {
                    if (!networkDetection.isAllowMobile()) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                            flowPlayLayout.setVisibility(View.VISIBLE);
                            cancelFlowPlayButton.setVisibility(View.GONE);
                        }
                    }
                } else if (networkDetection.isWifiType()) {
                    if (flowPlayLayout.getVisibility() == View.VISIBLE) {
                        flowPlayLayout.setVisibility(View.GONE);
                        if (videoView.isInPlaybackState()) {
                            videoView.start();
                        } else {
                            play(vid, bitrate, true, isMustFromLocal);
                        }
                    }
                }
            }
        });
    }

    private void addFragment() {
        danmuFragment = new PolyvPlayerDanmuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_danmu, danmuFragment, "danmuFragment");
        // 网校的在线视频才添加下面的控件
        if (!getIntent().getBooleanExtra(PolyvMainActivity.IS_VLMS_ONLINE, false)) {
            ft.commit();
            return;
        }
        ImageLoader.getInstance().displayImage(((PolyvCoursesInfo.Course) getIntent().getExtras().getParcelable("course")).cover_image, iv_vlms_cover = ((ImageView) findViewById(R.id.iv_vlms_cover)),
                new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.polyv_demo).showImageForEmptyUri(R.drawable.polyv_demo).showImageOnFail(R.drawable.polyv_demo)
                        .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).build());
        topFragment = new PolyvPlayerTopFragment();
        topFragment.setArguments(getIntent().getExtras());
        tabFragment = new PolyvPlayerTabFragment();
        viewPagerFragment = new PolyvPlayerViewPagerFragment();
        ft.add(R.id.fl_top, topFragment, "topFragmnet");
        ft.add(R.id.fl_tab, tabFragment, "tabFragment");
        ft.add(R.id.fl_viewpager, viewPagerFragment, "viewPagerFragment");
        ft.commit();
    }

    private void findIdAndNew() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        videoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        marqueeView = (PolyvMarqueeView) findViewById(R.id.polyv_marquee_view);
        mediaController = (PolyvPlayerMediaController) findViewById(R.id.polyv_player_media_controller);
        srtTextView = (TextView) findViewById(R.id.srt);
        topSrtTextView = (TextView) findViewById(R.id.top_srt);
        questionView = (PolyvPlayerAnswerView) findViewById(R.id.polyv_player_question_view);
        auditionView = (PolyvPlayerAuditionView) findViewById(R.id.polyv_player_audition_view);
        auxiliaryVideoView = (PolyvAuxiliaryVideoView) findViewById(R.id.polyv_auxiliary_video_view);
        auxiliaryLoadingProgress = (ProgressBar) findViewById(R.id.auxiliary_loading_progress);
        auxiliaryView = (PolyvPlayerAuxiliaryView) findViewById(R.id.polyv_player_auxiliary_view);
        advertCountDown = (TextView) findViewById(R.id.count_down);
        firstStartView = (PolyvPlayerPreviewView) findViewById(R.id.polyv_player_first_start_view);
        lightView = (PolyvPlayerLightView) findViewById(R.id.polyv_player_light_view);
        volumeView = (PolyvPlayerVolumeView) findViewById(R.id.polyv_player_volume_view);
        progressView = (PolyvPlayerProgressView) findViewById(R.id.polyv_player_progress_view);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        coverView = (PolyvPlayerAudioCoverView) findViewById(R.id.polyv_cover_view);
        audioSourceCoverView = (PolyvPlayerAudioCoverView) findViewById(R.id.polyv_source_audio_cover);
        fl_screencast_search = (PolyvScreencastSearchLayout) findViewById(R.id.fl_screencast_search);
        fl_screencast_search_land = (PolyvScreencastSearchLayout) findViewById(R.id.fl_screencast_search_land);
        fl_screencast_status = (PolyvScreencastStatusLayout) findViewById(R.id.fl_screencast_status);
        playErrorView = (PolyvPlayerPlayErrorView) findViewById(R.id.polyv_player_play_error_view);
        playRouteView = (PolyvPlayerPlayRouteView) findViewById(R.id.polyv_player_play_route_view);
        flowPlayLayout = (LinearLayout) findViewById(R.id.flow_play_layout);
        flowPlayButton = (TextView) findViewById(R.id.flow_play_button);
        cancelFlowPlayButton = (TextView) findViewById(R.id.cancel_flow_play_button);

        iv_screencast_search = (ImageView) mediaController.findViewById(R.id.iv_screencast_search);
        iv_screencast_search_land = (ImageView) mediaController.findViewById(R.id.iv_screencast_search_land);
        //投屏功能默认隐藏，如果需要请注释下面两行代码
        iv_screencast_search.setVisibility(View.GONE);
        iv_screencast_search_land.setVisibility(View.GONE);

        mediaController.initConfig(viewLayout);
        mediaController.setAudioCoverView(coverView);
        mediaController.setDanmuFragment(danmuFragment);
        questionView.setPolyvVideoView(videoView);
        auditionView.setPolyvVideoView(videoView);
        auxiliaryVideoView.setPlayerBufferingIndicator(auxiliaryLoadingProgress);
        auxiliaryView.setPolyvVideoView(videoView);
        auxiliaryView.setDanmakuFragment(danmuFragment);
        videoView.setMediaController(mediaController);
        videoView.setAuxiliaryVideoView(auxiliaryVideoView);
        videoView.setPlayerBufferingIndicator(loadingProgress);
        // 设置跑马灯
        videoView.setMarqueeView(marqueeView, marqueeItem = new PolyvMarqueeItem()
                .setStyle(PolyvMarqueeItem.STYLE_ROLL) //样式
                .setDuration(10000) //时长
                .setText("POLYV Android SDK") //文本
                .setSize(16) //字体大小
                .setColor(Color.YELLOW) //字体颜色
                .setTextAlpha(70) //字体透明度
                .setInterval(1000) //隐藏时间
                .setLifeTime(1000) //显示时间
                .setTweenTime(1000) //渐隐渐现时间
                .setHasStroke(true) //是否有描边
                .setBlurStroke(true) //是否模糊描边
                .setStrokeWidth(3) //描边宽度
                .setStrokeColor(Color.MAGENTA) //描边颜色
                .setReappearTime(3000) // 设置跑马灯再次出现的间隔
                .setStrokeAlpha(70)); //描边透明度
    }

    private void initView() {
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setOpenMarquee(true);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);
        videoView.setSeekType(PolyvSeekType.SEEKTYPE_NORMAL);
        videoView.setLoadTimeoutSecond(25);//加载超时时间，单位：秒
        videoView.setBufferTimeoutSecond(15);//缓冲超时时间，单位：秒
        videoView.disableScreenCAP(this, false);//防录屏开关，true为开启，如果开启防录屏，投屏功能将不可用

        videoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                if (videoView.getVideo() != null && videoView.getVideo().isMp3Source()) {
                    audioSourceCoverView.onlyShowCover(videoView);
                } else {
                    audioSourceCoverView.hide();
                }
                mediaController.preparedView();
                progressView.setViewMaxValue(videoView.getDuration());
                // 没开预加载在这里开始弹幕
                // danmuFragment.start();
            }
        });

        videoView.setOnPreloadPlayListener(new IPolyvOnPreloadPlayListener() {
            @Override
            public void onPlay() {
                // 开启预加载在这里开始弹幕
                danmuFragment.start();
            }
        });

        videoView.setOnInfoListener(new IPolyvOnInfoListener2() {
            @Override
            public boolean onInfo(int what, int extra) {
                switch (what) {
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_START:
                        danmuFragment.pause(false);
                        break;
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_END:
                        danmuFragment.resume(false);
                        break;
                }

                return true;
            }
        });

        videoView.setOnPlayPauseListener(new IPolyvOnPlayPauseListener() {
            @Override
            public void onPause() {
                coverView.stopAnimation();
                danmuFragment.pause();
            }

            @Override
            public void onPlay() {
                coverView.startAnimation();
            }

            @Override
            public void onCompletion() {
                coverView.stopAnimation();
            }
        });

        videoView.setOnChangeModeListener(new IPolyvOnChangeModeListener() {
            @Override
            public void onChangeMode(String changedMode) {
                coverView.changeModeFitCover(videoView, changedMode);
            }
        });

        videoView.setOnVideoTimeoutListener(new IPolyvOnVideoTimeoutListener() {
            @Override
            public void onBufferTimeout(int timeoutSecond, int times) {//在一个缓冲里，每超过设置的timeoutSecond都会回调一次
                Toast.makeText(PolyvPlayerActivity.this, "视频加载速度缓慢，请切换到低清晰度的视频或调整网络", Toast.LENGTH_LONG).show();
            }
        });

        videoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    Toast.makeText(PolyvPlayerActivity.this, "状态错误 " + status, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        videoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
            @Override
            public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                playErrorView.show(playErrorReason, videoView);
                return true;
            }
        });

        //为了能更好的统一错误处理，这个错误回调合并到setOnVideoPlayErrorListener(IPolyvOnVideoPlayErrorListener2)中，对应的错误类型是PolyvPlayErrorReason.VIDEO_ERROR。
        //为了向后兼容，以前的程序不受影响，当设置了这个错误回调时，setOnVideoPlayErrorListener(IPolyvOnVideoPlayErrorListener2)错误回调不会被触发。
        //没有设置这个错误回调时，setOnVideoPlayErrorListener(IPolyvOnVideoPlayErrorListener2)错误回调才会触发。
//        videoView.setOnErrorListener(new IPolyvOnErrorListener2() {
//            @Override
//            public boolean onError() {
//                playErrorView.show(PolyvPlayErrorReason.VIDEO_ERROR, videoView);
//                return true;
//            }
//        });

        videoView.setOnAdvertisementOutListener(new IPolyvOnAdvertisementOutListener2() {
            @Override
            public void onOut(@NonNull PolyvADMatterVO adMatter) {
                auxiliaryView.show(adMatter);
            }
        });

        videoView.setOnAdvertisementCountDownListener(new IPolyvOnAdvertisementCountDownListener() {
            @Override
            public void onCountDown(int num) {
                advertCountDown.setText("广告也精彩：" + num + "秒");
                advertCountDown.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd() {
                advertCountDown.setVisibility(View.GONE);
                auxiliaryView.hide();
            }
        });

        videoView.setOnAdvertisementEventListener(new IPolyvOnAdvertisementEventListener2() {
            @Override
            public void onShow(PolyvADMatterVO adMatter) {
                Log.i(TAG, "开始播放视频广告");
            }

            @Override
            public void onClick(PolyvADMatterVO adMatter) {
                if (!TextUtils.isEmpty(adMatter.getAddrUrl())) {
                    try {
                        new URL(adMatter.getAddrUrl());
                    } catch (MalformedURLException e1) {
                        Log.e(TAG, PolyvSDKUtil.getExceptionFullMessage(e1, -1));
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(adMatter.getAddrUrl()));
                    startActivity(intent);
                }
            }
        });

        videoView.setOnQuestionOutListener(new IPolyvOnQuestionOutListener2() {
            @Override
            public void onOut(@NonNull PolyvQuestionVO questionVO) {
                switch (questionVO.getType()) {
                    case PolyvQuestionVO.TYPE_QUESTION:
                        questionView.showAnswerContent(questionVO);
                        break;

                    case PolyvQuestionVO.TYPE_AUDITION:
                        auditionView.show(questionVO);
                        break;
                }
            }
        });

        videoView.setOnTeaserOutListener(new IPolyvOnTeaserOutListener() {
            @Override
            public void onOut(@NonNull String url) {
                auxiliaryView.show(url);
            }
        });

        videoView.setOnTeaserCountDownListener(new IPolyvOnTeaserCountDownListener() {
            @Override
            public void onEnd() {
                auxiliaryView.hide();
            }
        });

        videoView.setOnQuestionAnswerTipsListener(new IPolyvOnQuestionAnswerTipsListener() {

            @Override
            public void onTips(@NonNull String msg) {
                questionView.showAnswerTips(msg);
            }

            @Override
            public void onTips(@NonNull String msg, int seek) {
                questionView.showAnswerTips(msg,seek);
            }
        });

        videoView.setOnCompletionListener(new IPolyvOnCompletionListener2() {
            @Override
            public void onCompletion() {
                danmuFragment.pause();
            }
        });

        videoView.setOnVideoSRTPreparedListener(new IPolyvOnVideoSRTPreparedListener() {
            @Override
            public void onVideoSRTPrepared() {
                mediaController.preparedSRT(videoView);
            }
        });

        videoView.setOnVideoSRTListener(new IPolyvOnVideoSRTListener() {
            @Override
            public void onVideoSRT(@Nullable List<PolyvSRTItemVO> subTitleItems) {
                srtTextView.setText("");
                topSrtTextView.setText("");

                if (subTitleItems != null) {
                    for (PolyvSRTItemVO srtItemVO : subTitleItems) {
                        if (srtItemVO.isBottomCenterSubTitle()) {
                            srtTextView.setText(srtItemVO.getSubTitle());
                        } else if (srtItemVO.isTopCenterSubTitle()) {
                            topSrtTextView.setText(srtItemVO.getSubTitle());
                        }
                    }
                }

                srtTextView.setVisibility(View.VISIBLE);
                topSrtTextView.setVisibility(View.VISIBLE);
            }
        });

        videoView.setOnGestureLeftUpListener(new IPolyvOnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness(PolyvPlayerActivity.this)));
                if(mediaController.isLocked()){
                    return;
                }

                int brightness = videoView.getBrightness(PolyvPlayerActivity.this) + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(PolyvPlayerActivity.this, brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness(PolyvPlayerActivity.this)));
                if(mediaController.isLocked()){
                    return;
                }
                int brightness = videoView.getBrightness(PolyvPlayerActivity.this) - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(PolyvPlayerActivity.this, brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureRightUpListener(new IPolyvOnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                if(mediaController.isLocked()){
                    return;
                }
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureRightDownListener(new IPolyvOnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                if(mediaController.isLocked()){
                    return;
                }
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureSwipeLeftListener(new IPolyvOnGestureSwipeLeftListener() {

            @Override
            public void callback(boolean start, int times, boolean end) {
                // 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if(mediaController.isLocked()){
                    return;
                }
                mediaController.hideTickTips();
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    videoView.seekTo(fastForwardPos);
                    danmuFragment.seekTo();
                    if (videoView.isCompletedState()) {
                        videoView.start();
                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 1000 * times;
                    if (fastForwardPos <= 0)
                        fastForwardPos = -1;
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, false);
            }
        });

        videoView.setOnGestureSwipeRightListener(new IPolyvOnGestureSwipeRightListener() {

            @Override
            public void callback(boolean start, int times, boolean end) {
                // 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if(mediaController.isLocked()){
                    return;
                }
                mediaController.hideTickTips();
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                    if (!videoView.isCompletedState()) {
                        videoView.seekTo(fastForwardPos);
                        danmuFragment.seekTo();
                    } else if (videoView.isCompletedState() && fastForwardPos != videoView.getDuration()) {
                        videoView.seekTo(fastForwardPos);
                        danmuFragment.seekTo();
                        videoView.start();
                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 1000 * times;
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, true);
            }
        });

        videoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean start, boolean end) {
                if ((videoView.isInPlaybackState() || videoView.isExceptionCompleted()) && mediaController != null)
                    if (mediaController.isShowing())
                        mediaController.hide();
                    else
                        mediaController.show();
            }
        });

        videoView.setOnGestureDoubleClickListener(new IPolyvOnGestureDoubleClickListener() {
            @Override
            public void callback() {
                if ((videoView.isInPlaybackState() || videoView.isExceptionCompleted()) && mediaController != null && !mediaController.isLocked())
                    mediaController.playOrPause();
            }
        });

        flowPlayButton.setOnClickListener(flowButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkDetection.allowMobile();
                flowPlayLayout.setVisibility(View.GONE);
                play(vid, bitrate, true, isMustFromLocal);
            }
        });
        cancelFlowPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowPlayLayout.setVisibility(View.GONE);
                videoView.start();
            }
        });
    }

    /**
     * 初始化视频播放错误提示界面
     */
    private void initPlayErrorView() {
        playErrorView.setRetryPlayListener(new PolyvPlayerPlayErrorView.IRetryPlayListener() {
            @Override
            public void onRetry() {
                play(vid, bitrate, true, isMustFromLocal);
            }
        });

        playErrorView.setShowRouteViewListener(new PolyvPlayerPlayErrorView.IShowRouteViewListener() {
            @Override
            public void onShow() {
                playRouteView.show(videoView);
            }
        });
    }

    /**
     * 初始化线路切换界面
     */
    private void initRouteView() {
        playRouteView.setChangeRouteListener(new PolyvPlayerPlayRouteView.IChangeRouteListener() {
            @Override
            public void onChange(int route) {
                playErrorView.hide();
                videoView.changeRoute(route);
            }
        });
    }

    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        this.vid = vid;
        this.bitrate = bitrate;
        this.isMustFromLocal = isMustFromLocal;
        if (TextUtils.isEmpty(vid)) return;

        if (networkDetection.isMobileType() && !networkDetection.isAllowMobile()) {
            if (PolyvDownloader.FILE_VIDEO == fileType) {
                if (bitrate != 0 && !PolyvVideoUtil.validateLocalVideo(vid, bitrate).hasLocalVideo() ||
                        (bitrate == 0 && !PolyvVideoUtil.validateLocalVideo(vid).hasLocalVideo())) {
                    flowPlayButton.setOnClickListener(flowButtonOnClickListener);
                    flowPlayLayout.setVisibility(View.VISIBLE);
                    cancelFlowPlayButton.setVisibility(View.GONE);
                    return;
                }
            } else {
                if (bitrate != 0 && PolyvVideoUtil.validateMP3Audio(vid, bitrate) == null && !PolyvVideoUtil.validateLocalVideo(vid, bitrate).hasLocalVideo() ||
                        (bitrate == 0 && PolyvVideoUtil.validateMP3Audio(vid).size() == 0) && !PolyvVideoUtil.validateLocalVideo(vid).hasLocalVideo()) {
                    flowPlayButton.setOnClickListener(flowButtonOnClickListener);
                    flowPlayLayout.setVisibility(View.VISIBLE);
                    cancelFlowPlayButton.setVisibility(View.GONE);
                    return;
                }
            }
        }

        if (fl_screencast_status != null && fl_screencast_status.getVisibility() == View.VISIBLE) {
            showScreencastTipsDialog(vid, bitrate, startNow, isMustFromLocal);
            return;
        }

        if (iv_vlms_cover != null && iv_vlms_cover.getVisibility() == View.VISIBLE) {
            iv_vlms_cover.setVisibility(View.GONE);
        }

        if (videoView.isDisableScreenCAP()) {
            iv_screencast_search.setVisibility(View.GONE);
            iv_screencast_search_land.setVisibility(View.GONE);
        }

        videoView.release();
        srtTextView.setVisibility(View.GONE);
        topSrtTextView.setVisibility(View.GONE);
        mediaController.hide();
        mediaController.resetView();
        loadingProgress.setVisibility(View.GONE);
        questionView.hide();
        auditionView.hide();
        auxiliaryVideoView.hide();
        auxiliaryLoadingProgress.setVisibility(View.GONE);
        auxiliaryView.hide();
        advertCountDown.setVisibility(View.GONE);
        firstStartView.hide();
        progressView.resetMaxValue();
        audioSourceCoverView.hide();

        danmuFragment.setVid(vid, videoView);
        if (PolyvDownloader.FILE_VIDEO == fileType) {
            videoView.setPriorityMode(PolyvVideoVO.MODE_VIDEO);
        } else if (PolyvDownloader.FILE_AUDIO == fileType) {
            videoView.setPriorityMode(PolyvVideoVO.MODE_AUDIO);
        }
        if (startNow) {
            //调用setVid方法视频会自动播放
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            //视频不播放，先显示一张缩略图
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    /**
                     * 调用setVid方法视频会自动播放
                     * 如果是有学员登陆的播放，可以在登陆的时候通过
                     * {@link com.easefun.polyvsdk.PolyvSDKClient.getinstance().setViewerId()}设置学员id
                     * 或者调用{@link videoView.setVidWithStudentId}传入学员id进行播放
                     */

                    videoView.setVidWithStudentId(vid, bitrate, isMustFromLocal,"123");
                }
            });

            firstStartView.show(vid);
        }
        if (PolyvVideoVO.MODE_VIDEO.equals(videoView.getPriorityMode())) {
            coverView.hide();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (viewPagerFragment != null)
            viewPagerFragment.getTalkFragment().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBackgroundPlay) {
            //回来后继续播放
            if (isPlay) {
                videoView.onActivityResume();
                danmuFragment.resume();
                if (auxiliaryView.isPauseAdvert()) {
                    auxiliaryView.hide();
                }
            }
        }
        mediaController.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaController.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isBackgroundPlay) {
            //弹出去暂停
            isPlay = videoView.onActivityStop();
            danmuFragment.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
        questionView.hide();
        auditionView.hide();
        auxiliaryView.hide();
        firstStartView.hide();
        coverView.hide();
        mediaController.disable();
        fl_screencast_search.destroy();
        fl_screencast_search_land.destroy();
        screencastHelper.release();
        networkDetection.destroy();
    }

    private void showScreencastTipsDialog(final String vid, final int bitrate, final boolean startNow, final boolean isMustFromLocal) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("切换视频后会退出当前的投屏，是否继续")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fl_screencast_status.hide(true);
                        play(vid, bitrate, startNow, isMustFromLocal);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void initScreencast() {
        fl_screencast_status.setScreencastSearchLayout(fl_screencast_search);
        fl_screencast_status.setLandScreencastSearchLayout(fl_screencast_search_land);
        fl_screencast_status.setVideoView(videoView);
        fl_screencast_status.setMediaController(mediaController);

        screencastHelper = PolyvScreencastHelper.getInstance(null);//如果之前已经初始化，那么这里可以传null

        fl_screencast_search.setScreencastStatusLayout(fl_screencast_status);
        fl_screencast_search.setScreencastHelper(screencastHelper);
        fl_screencast_search_land.setScreencastStatusLayout(fl_screencast_status);
        fl_screencast_search_land.setScreencastHelper(screencastHelper);

        iv_screencast_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_screencast_search.isSelected()) {
                    fl_screencast_search.hide(true);
                } else {
                    fl_screencast_search.show();
                }
            }
        });
        iv_screencast_search_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_screencast_search_land.show();
            }
        });

        fl_screencast_search.setOnVisibilityChangedListener(new PolyvScreencastSearchLayout.OnVisibilityChangedListener() {
            @Override
            public void onVisibilityChanged(@NonNull View changedView, int visibility) {
                iv_screencast_search.setSelected(visibility == View.VISIBLE);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && fl_screencast_search_land.getVisibility() == View.VISIBLE) {
            int[] location = new int[2];
            fl_screencast_search_land.getLocationInWindow(location);
            if (ev.getX() < location[0]) {
                fl_screencast_search_land.hide(true);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fl_screencast_search.getVisibility() == View.VISIBLE) {
                fl_screencast_search.hide(true);
                return true;
            }
            if (fl_screencast_search_land.getVisibility() == View.VISIBLE) {
                fl_screencast_search_land.hide(true);
                return true;
            }
            if (mediaController != null && mediaController.isLocked()) {
                return true;
            }
            if (PolyvScreenUtils.isLandscape(this) && mediaController != null) {
                mediaController.changeToPortrait();
                return true;
            }
            if (viewPagerFragment != null && PolyvScreenUtils.isPortrait(this) && viewPagerFragment.isSideIconVisible()) {
                viewPagerFragment.setSideIconVisible(false);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid) {
        return newIntent(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate) {
        return newIntent(context, playMode, vid, bitrate, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow) {
        return newIntent(context, playMode, vid, bitrate, startNow, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
                                   boolean isMustFromLocal) {
        return newIntent(context, playMode, vid, bitrate, startNow, isMustFromLocal, PolyvDownloader.FILE_VIDEO);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
                                   boolean isMustFromLocal, int fileType) {
        Intent intent = new Intent(context, PolyvPlayerActivity.class);
        intent.putExtra("playMode", playMode.getCode());
        intent.putExtra("value", vid);
        intent.putExtra("bitrate", bitrate);
        intent.putExtra("startNow", startNow);
        intent.putExtra("isMustFromLocal", isMustFromLocal);
        intent.putExtra("fileType", fileType);
        return intent;
    }

    public static void intentTo(Context context, PlayMode playMode, String vid) {
        intentTo(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate) {
        intentTo(context, playMode, vid, bitrate, false);
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow) {
        intentTo(context, playMode, vid, bitrate, startNow, false);
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
                                boolean isMustFromLocal) {
        context.startActivity(newIntent(context, playMode, vid, bitrate, startNow, isMustFromLocal));
    }

    /**
     * 播放模式
     *
     * @author TanQu
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }
}

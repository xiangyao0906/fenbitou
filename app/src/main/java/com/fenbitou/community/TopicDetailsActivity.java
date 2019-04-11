package com.fenbitou.community;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.adapter.TopicCommentAdapter;
import com.fenbitou.community.interfaceUtils.OnSingleItemClicListener;
import com.fenbitou.community.utils.Address;
import com.fenbitou.community.utils.HtmlImageGetter;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.MessageCallback;
import com.fenbitou.entity.MessageEntity;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.EmojiFilter;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.ShareDialog;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;

public class TopicDetailsActivity extends BaseActivity
        implements View.OnClickListener, PlatformActionListener, Handler.Callback, AbsListView.OnScrollListener, OnSingleItemClicListener {

    private static final String ERRORINFO = "加载失败了(⊙o⊙)，点击重新加载";
    private TextView title_text;// title
    private LinearLayout back_layout;// back
    private ImageView topic_details_avatar;// 用户头像
    private TextView topicTop, topicEssence, topicFiery, topic_praise, all_comment, topic_details_author,
            topic_group_name, topic_details_title, topic_details_createTime, topic_details_browse,
            topic_details_comment, topic_details_praise, loading; // 是否置顶,点赞数,全部评论,用户昵称，用户姓名，话题的标题，话题创建的时间，话题浏览数，话题评论，话题点赞
    private TextView topic_details_content;// 话题内容
    private LinearLayout comment_layout, praise_layout, share_layout;
    private ProgressDialog progressDialog;// loading
    private int topicId;// 话题Id
    private int currentPage = 1;// 当前页
    private int userId;// 用户id
    private ListView topic_comment_listView;// 评论列表
    private List<EntityPublic> commentDtoList;// 评论集合
    private int isTop, isEssence, isFiery;// 是否置顶，是否精华，是否火热
    private int counts;
    private boolean isCounts, isPraise, isCallBack;
    private ShareDialog shareDialog; // 分享
    private ProgressBar load_progressBar;
    private boolean isLoad;
    private EntityPublic entityPublic;
    private boolean listSetting;//标记后台是否设置只限本组员浏览
    private LinearLayout topicDetails_lin, topicHeader_lin, topicDetails_default;
    private TopicCommentAdapter adapter;

    //网络连接
    @Override
    protected int initContentView() {
        return R.layout.activity_topic_details;
    }

    // 初始化组件
    @Override
    protected void initComponent() {
        Bundle intent = getIntent().getExtras();
        topicId = intent.getInt("topicId", 0);
        isTop = intent.getInt("isTop", 0);
        isEssence = intent.getInt("isEssence", 0);
        isFiery = intent.getInt("isFiery", 0);
        listSetting = intent.getBoolean("listSetting", false);
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("话题详情");// 设置标题
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        topic_details_comment = (TextView) findViewById(R.id.topic_details_comment);
        topic_details_praise = (TextView) findViewById(R.id.topic_details_praise);
        comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
        praise_layout = (LinearLayout) findViewById(R.id.praise_layout);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        topic_comment_listView = (ListView) findViewById(R.id.topic_comment_listView);
        topicDetails_lin = (LinearLayout) findViewById(R.id.topicDetails_lin);
        topicDetails_default = (LinearLayout) findViewById(R.id.topicDetails_default);
        commentDtoList = new ArrayList<EntityPublic>();
        progressDialog = new ProgressDialog(this);
        View headerView = LayoutInflater.from(TopicDetailsActivity.this).inflate(R.layout.topic_details_header, null);
        View footView = LayoutInflater.from(TopicDetailsActivity.this).inflate(R.layout.hot_listview_foot_view, null);
        loading = (TextView) footView.findViewById(R.id.loading);
        load_progressBar = (ProgressBar) footView.findViewById(R.id.load_progressBar);
        topicHeader_lin = (LinearLayout) headerView.findViewById(R.id.topicHeader_lin);
        topicTop = (TextView) headerView.findViewById(R.id.topicTop);
        topicEssence = (TextView) headerView.findViewById(R.id.topicEssence);
        topicFiery = (TextView) headerView.findViewById(R.id.topicFiery);
        topic_details_avatar = (ImageView) headerView.findViewById(R.id.topic_details_avatar);
        topic_details_author = (TextView) headerView.findViewById(R.id.topic_details_author);
        topic_group_name = (TextView) headerView.findViewById(R.id.topic_group_name);
        topic_details_title = (TextView) headerView.findViewById(R.id.topic_details_title);
        topic_details_createTime = (TextView) headerView.findViewById(R.id.topic_details_createTime);
        topic_details_browse = (TextView) headerView.findViewById(R.id.topic_details_browse);
        topic_details_content = (TextView) headerView.findViewById(R.id.topic_details_content);
        topic_praise = (TextView) headerView.findViewById(R.id.topic_praise);
        all_comment = (TextView) headerView.findViewById(R.id.all_comment);
        topic_comment_listView.addHeaderView(headerView);
        topic_comment_listView.addFooterView(footView);
        if (isTop == 1) {
            topicTop.setVisibility(View.VISIBLE);
        }
        if (isEssence == 1) {
            topicEssence.setVisibility(View.VISIBLE);
        }
        if (isFiery == 1) {
            topicFiery.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {
        loading.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        topic_praise.setOnClickListener(this);
        praise_layout.setOnClickListener(this);
        comment_layout.setOnClickListener(this);
        share_layout.setOnClickListener(this);
        topic_group_name.setOnClickListener(this);
        topic_comment_listView.setOnScrollListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        commentDtoList.clear();

        getTopicDetails(userId, topicId);
    }


    // 获取话题详情
    private void getTopicDetails(final int userId, final int topicId) {
        showLoading(TopicDetailsActivity.this);
        OkHttpUtils.get().addParams("topicId", String.valueOf(topicId)).addParams("userId", String.valueOf(userId)).url(Address.TOPICINFO).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                try {
                    if (response.isSuccess()) {
                        counts = response.getEntity().getTopic().getPraiseCounts();
                        entityPublic = response.getEntity().getTopic();
                        boolean isyes = response.getEntity().isYes();
                        if (listSetting) {
                            if (!isyes) {
                                topicDetails_default.setVisibility(View.VISIBLE);
                                topicHeader_lin.setVisibility(View.GONE);
                                topic_comment_listView.setVisibility(View.GONE);
                                topicDetails_lin.setVisibility(View.GONE);
                            } else {
                                topicDetails_default.setVisibility(View.GONE);
                                topicDetails_lin.setVisibility(View.VISIBLE);
                            }
                        } else {
                            topicDetails_lin.setVisibility(View.VISIBLE);
                        }
                        isCounts = true;
                        if (isCallBack) {
                            all_comment
                                    .setText(String.valueOf(response.getEntity().getTopic().getCommentCounts()));
                            topic_details_comment
                                    .setText(String.valueOf(response.getEntity().getTopic().getCommentCounts()));
                        } else {
                            GlideUtil.loadCircleImage(TopicDetailsActivity.this, Address.IMAGE + response.getEntity().getUserExpandDto().getAvatar(), topic_details_avatar);
                            topic_details_author.setText(response.getEntity().getUserExpandDto().getNickname());
                            topic_group_name.setText(response.getEntity().getGroup().getName());
                            topic_praise
                                    .setText(String.valueOf(response.getEntity().getTopic().getPraiseCounts()));
                            all_comment
                                    .setText(String.valueOf(response.getEntity().getTopic().getCommentCounts()));
                            topic_details_title.setText(response.getEntity().getTopic().getTitle());
                            topic_details_createTime.setText(response.getEntity().getTopic().getCreateTime());
                            topic_details_browse.setText(String.valueOf(response.getEntity().getTopic().getBrowseCounts()));

                            Drawable defaultDrawable = TopicDetailsActivity.this.getResources()
                                    .getDrawable(R.drawable.sprite);
                            topic_details_content.setText(Html.fromHtml(response.getEntity().getTopic().getContent(), new HtmlImageGetter(TopicDetailsActivity.this, topic_details_content, "/esun_msg",
                                    defaultDrawable,false),null));
                            topic_details_content.setMovementMethod(LinkMovementMethod.getInstance());


                            topic_details_comment.setText(String.valueOf(response.getEntity().getTopic().getCommentCounts()));
                            topic_details_praise.setText(String.valueOf(response.getEntity().getTopic().getPraiseCounts()));
                            isCallBack = true;
                        }
                        getTopicCommentList(userId, topicId, currentPage);
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    // 话题评论
    private void getTopicCommentList(int userId, int topicId, final int currentPage) {
        showLoading(TopicDetailsActivity.this);
        OkHttpUtils.get().addParams("topicId", String.valueOf(topicId))
                .addParams("userId", String.valueOf(userId))
                .addParams("page.currentPage", String.valueOf(currentPage))
                .url(Address.TOPICCOMMENTLIST)
                .build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                if (response.isSuccess()) {
                    PageEntity pageEntity = response.getEntity().getPage();
                    if (pageEntity.getTotalPageSize() <= currentPage) {
                        isLoad = true;
                        load_progressBar.setVisibility(View.GONE);
                        loading.setText("没有更多了╮(╯▽╰)╭");
                    } else {
                        load_progressBar.setVisibility(View.VISIBLE);
                        loading.setText("正在加载...");
                        isLoad = false;
                    }
                    List<EntityPublic> tempCommentDtoList = response.getEntity().getCommentDtoList();
                    if (tempCommentDtoList != null && tempCommentDtoList.size() > 0) {
                        for (int i = 0; i < tempCommentDtoList.size(); i++) {
                            commentDtoList.add(tempCommentDtoList.get(i));
                        }
                    }
                    if (adapter == null) {
                        adapter = new TopicCommentAdapter(TopicDetailsActivity.this, commentDtoList);
                        adapter.setOnSingleItemClicListener(TopicDetailsActivity.this);
                        topic_comment_listView.setAdapter(adapter);
                    } else {
                        adapter.setCommentDtoList(commentDtoList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }


    // 话题点赞
    private void topicPraise(int topicId, int userId) {


        OkHttpUtils.get()
                .addParams("topicId", String.valueOf(topicId))
                .addParams("userId", String.valueOf(userId))
                .url(Address.TOPICPRAISE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        /*
                         * 这里使用Gson 莫名其妙会报错
                         *
                         * **/
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean abc = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            if (abc) {
                                int temp = counts++;
                                isPraise = true;
                                topic_praise.setText(String.valueOf(temp + 1));
                                topic_details_praise.setText(String.valueOf(temp + 1));
                                IToast.show(TopicDetailsActivity.this, message);
                            } else {
                                IToast.show(TopicDetailsActivity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            // 当列表滚动停止
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                // 最后一个view的下标
                int lastVisibleItem = view.getLastVisiblePosition() - 1;
                // 当前listView的总数量
                int totalItemCount = adapter.getCount();
                // 当用户滑动到最后一个View并且手指离开屏幕并且没有加载过则加载更多
                if (lastVisibleItem == totalItemCount && !isLoad) {
                    // true 已经加载过了，防止之前请求的数据没有加载完成，用户再次滑动，导致多次请求
                    isLoad = true;
                    currentPage++;
                    getTopicCommentList(userId, topicId, currentPage);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                TopicDetailsActivity.this.finish();
                break;
            case R.id.topic_praise:
                if (userId == 0) {
                    Toast.makeText(TopicDetailsActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                } else {
                    if (isCounts) {
                        if (!isPraise) {
                            topicPraise(topicId, userId);
                        } else {
                            Toast.makeText(TopicDetailsActivity.this, "你已经点过赞了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.praise_layout:
                if (userId == 0) {
                    Toast.makeText(TopicDetailsActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                } else {
                    if (isCounts) {
                        if (!isPraise) {
                            topicPraise(topicId, userId);
                        } else {
                            Toast.makeText(TopicDetailsActivity.this, "你已经点过赞了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.comment_layout:

                showCommentDialog();

//              没有必要再跳转一个页面
//              Bundle bundle=new Bundle();
//              bundle.putInt("topicId", topicId);
//              openActivity(AddCommentActivity.class,bundle);
                break;
            case R.id.share_layout:
                if (userId == 0) {
                    Toast.makeText(TopicDetailsActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                } else {
                    if (shareDialog == null) {
                        shareDialog = new ShareDialog(this, R.style.custom_dialog);
                        shareDialog.setCancelable(false);
                        shareDialog.show();
                        shareDialog.shareInfo(entityPublic, false, false, false, true);
                    } else {
                        shareDialog.show();
                    }
                }
                break;
            case R.id.topic_group_name:
                // intent.setClass(TopicDetailsActivity.this,
                // GroupDetailActivity.class);
                // intent.putExtra("GroupId",
                // topicEntity.getEntity().getGroup().getId());
                // startActivity(intent);
                break;
            case R.id.loading:
                // 如果加载失败，点击重新加载
                if (loading.getText().equals(ERRORINFO)) {
                    load_progressBar.setVisibility(View.VISIBLE);
                    loading.setText("正在加载...");
                    getTopicCommentList(userId, topicId, currentPage);
                }
                break;
            default:
                break;
        }
    }

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 == 0) { // qq好友
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle("测试分享的标题");
            sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
            sp.setText("Text文本内容 http://www.baidu.com");
            Platform qzone = ShareSDK.getPlatform(QQ.NAME);
            qzone.setPlatformActionListener(this); // 设置分享事件回调
            // 执行图文分享
            qzone.share(sp);
        } else if (arg2 == 1) { // qq空间
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle("测试分享的标题");
            sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
            sp.setText("Text文本内容 http://www.baidu.com");
            sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            sp.setSite("sharesdk");
            sp.setSiteUrl("http://sharesdk.cn");
            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
            qzone.setPlatformActionListener(this); // 设置分享事件回调
            // 执行图文分享
            qzone.share(sp);
        } else if (arg2 == 2) { // 微信好友
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setText("测试分享的文本");
            Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
            weibo.setPlatformActionListener(this); // 设置分享事件回调
            // 执行图文分享
            weibo.share(sp);
        } else if (arg2 == 3) { // 微信朋友圈
            Platform.ShareParams sp = new Platform.ShareParams();
            Platform weibo = ShareSDK.getPlatform(WechatMoments.NAME);
            sp.setText("Text文本内容 http://www.baidu.com");
            sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            weibo.setPlatformActionListener(this); // 设置分享事件回调
            // 执行图文分享
            weibo.share(sp);
        } else if (arg2 == 4) { // 新浪微博
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setText("测试分享的文本");
            // sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.setPlatformActionListener(this); // 设置分享事件回调
            // 执行图文分享
            weibo.share(sp);
        }
    }

    @Override
    public void onCancel(Platform platform, int arg1) {
        // 取消
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = arg1;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
        Log.i("lala", "oncancel............");
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
        // 成功
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
        Log.i("lala", "onComplete............");
    }

    @Override
    public void onError(Platform arg0, int action, Throwable t) {
        // 失敗
        // 打印错误信息,print the error msg
        t.printStackTrace();
        // 错误监听,handle the error msg
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
        Log.i("lala", "onError............");
    }

    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: {
                // 成功
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                System.out.println("分享回调成功------------");
            }
            break;
            case 2: {
                // 失败
                Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: {
                // 取消
                Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }

        return false;

    }

    @Override
    public void OnItemClickListener(int position) {
        //评论的点赞事件 需要判断是否点过赞
        commentPraise(commentDtoList.get(position).getId(), position);

    }

    // 评论点赞
    private void commentPraise(int commentId, final int position) {

        OkHttpUtils.get()
                .addParams("commentId", String.valueOf(commentId))
                .url(Address.COMMENTPRAISE)
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        if (response.isSuccess()) {
                            int counts = commentDtoList.get(position).getPraiseNumber();
                            int temp = counts++;
                            commentDtoList.get(position).setPraiseNumber((temp + 1));
                            adapter.notifyDataSetChanged();
                            IToast.show(TopicDetailsActivity.this, "点赞成功");
                        } else {
                            IToast.show(TopicDetailsActivity.this, "点赞失败");
                        }
                    }
                });
    }

    /**
     * @Description: 输入评论的dialog
     */
    public void showCommentDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.commit_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.edit_input);
                        editText.setFilters(new InputFilter[]{new EmojiFilter()});
                        TextView textView = holder.getView(R.id.sencontet);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                    IToast.show(TopicDetailsActivity.this, "请输入评论内容！");
                                } else {
                                    submitComment(topicId, userId, editText.getText().toString().trim());
                                }
                                dialog.dismiss();
                            }
                        });
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, 0);
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }


    // 提交话题评论
    private void submitComment(final int topicId, final int userId, String commentContent) {

        OkHttpUtils.get()
                .addParams("topicId", String.valueOf(topicId))
                .addParams("userId", String.valueOf(userId))
                .addParams("commentContent", commentContent)
                .url(Address.CREATECOMMENT)
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                IToast.show(TopicDetailsActivity.this, response.getMessage());
                                commentDtoList.clear();
                                getTopicCommentList(userId, topicId, 1);

                            } else {
                                if (TextUtils.isEmpty(response.getMessage())) {
                                    IToast.show(TopicDetailsActivity.this, "评论失败！");
                                } else {
                                    IToast.show(TopicDetailsActivity.this, response.getMessage());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }


}

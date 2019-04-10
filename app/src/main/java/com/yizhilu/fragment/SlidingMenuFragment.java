package com.yizhilu.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.LoginActivity;
import com.yizhilu.eduapp.MyCourseActivity;
import com.yizhilu.eduapp.OpinionFeedbackActivity;
import com.yizhilu.eduapp.PersonalInformationActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.eduapp.StudyStatisticsActivity;
import com.yizhilu.eduapp.SystemSettingActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2017/6/29.
 * 侧边栏
 */

public class SlidingMenuFragment extends BaseFragment {

    @BindView(R.id.user_head_image)
    ImageView userHeadImage;
    @BindView(R.id.user_name_text)
    TextView userName;
    @BindViews({R.id.my_order_layout,R.id.personal_resume_layout,R.id.my_course_layout,
            R.id.my_live_layout,R.id.system_msg_layout,R.id.industry_information_layout,
            R.id.course_teacher_layout,R.id.my_collection_layout,R.id.discount_coupon_layout,R.id.my_account_layout,
            R.id.opinion_feedback,R.id.system_set_linear})
    List<LinearLayout> layoutList;
    @BindView(R.id.my_account_text)
    TextView myAccountText;
    private int userId;

    @Override
    protected int initContentView() {
        return R.layout.fragment_sliding_meun;
    }

    @Override
    protected void initComponent() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        if (userId == -1){
            userHeadImage.setImageResource(R.drawable.head_bg); // 修改用户头像
            myAccountText.setText("");
            userName.setText("未登录");
        }else {
            getUserMessage();
        }
        super.onResume();
    }

    /**
     * 获取用户信息
     */
    private void getUserMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("userId",String.valueOf(userId));
        ILog.i(Address.MY_MESSAGE+map+"----------获取用户信息");
        OkHttpUtils.post().params(map).url(Address.MY_MESSAGE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                EntityPublic userExpandDto = response.getEntity().getUserExpandDto();
                                String showName = userExpandDto.getShowname();
                                String email = userExpandDto.getEmail();
                                String mobile = userExpandDto.getMobile();
                                if(!TextUtils.isEmpty(showName)){
                                    userName.setText(showName);
                                }else if(!TextUtils.isEmpty(email)){
                                    userName.setText(email);
                                }else{
                                    userName.setText(mobile);
                                }
                                String balance = response.getEntity().getBalance();
                                myAccountText.setText("余额 : ￥ "+ balance);

                                GlideUtil.loadCircleHeadImage(getActivity(),
                                        Address.IMAGE_NET + userExpandDto.getAvatar(),
                                        userHeadImage);
                            }else {
                                IToast.show(getActivity(),response.getMessage());
                            }
                        }catch (Exception e){
                            ILog.i(e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.user_head_image,
            R.id.my_order_layout,R.id.personal_resume_layout,R.id.my_course_layout,
            R.id.my_live_layout,R.id.system_msg_layout,R.id.industry_information_layout,
            R.id.course_teacher_layout,R.id.my_collection_layout,R.id.discount_coupon_layout,R.id.my_account_layout,
            R.id.opinion_feedback,R.id.system_set_linear})
    public void onClick(View v){
        if (userId == -1) {
            // TODO: 2017/7/5 登录 
            openActivity(LoginActivity.class);
            return;
        }
        switch (v.getId()){
            case R.id.user_head_image: // 用户头像
                openActivity(PersonalInformationActivity.class);
                break;
            case R.id.my_order_layout: // 我的订单
                break;
            case R.id.personal_resume_layout:  //学习统计
                openActivity(StudyStatisticsActivity.class);
                break;
            case R.id.my_course_layout: // 我的课程
                openActivity(MyCourseActivity.class);
                break;
            case R.id.my_live_layout: //我的直播
                break;
            case R.id.system_msg_layout: // 系统公告
                break;
            case R.id.industry_information_layout: // 行业资讯
                break;
            case R.id.course_teacher_layout: // 课程讲师
                break;
            case R.id.my_collection_layout: // 我的收藏
                break;
            case R.id.discount_coupon_layout:  //优惠券
                break;
            case R.id.my_account_layout: // 我的账户
                break;
            case R.id.opinion_feedback: // 意见反馈
                openActivity(OpinionFeedbackActivity.class);
                break;
            case R.id.system_set_linear: // 系统设置
                openActivity(SystemSettingActivity.class);
                break;
        }
    }
}

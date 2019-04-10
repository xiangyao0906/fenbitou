package com.yizhilu.fragment;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.adapter.CourseCommentAdapter;
import com.yizhilu.base.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2017/7/3.
 * 课程评论
 */

public class CourseCommentFragment extends BaseFragment {

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.discuss_edit_text)
    EditText discussEditText;
    @BindView(R.id.send_message)
    TextView sendMessage;
    @BindView(R.id.no_discuss_layout)
    LinearLayout noDiscussLayout;
    @BindView(R.id.discuss_list_view)
    NoScrollListView discussListView;

    private List<EntityPublic> list;
    private CourseCommentAdapter courseCommentAdapter;

    private int userId,courseId;
    private EntityPublic entityPublic;
    private int currentPage = 1;
    private int kpointId;

    @Override
    protected int initContentView() {
        return R.layout.fragment_course_comment;
    }

    @Override
    protected void initComponent() {
        list = new ArrayList<>();
        courseCommentAdapter = new CourseCommentAdapter(getActivity(),list);
        discussListView.setAdapter(courseCommentAdapter);
        discussListView.setEmptyView(noDiscussLayout);
        swipeToLoadLayout.setRefreshEnabled(false);
    }

    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        entityPublic = (EntityPublic) getArguments().getSerializable("entity");
        if (entityPublic == null) return;
        kpointId = entityPublic.getDefaultKpointId();  //得到课程节点的Id
        courseId = entityPublic.getCourse().getId();
        getCourseComment();
    }

    private void getCourseComment() {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", String.valueOf(courseId));
        map.put("page.currentPage", String.valueOf(currentPage));
        OkHttpUtils.post().params(map).url(Address.COURSE_COMMENT_LIST).build().execute(
                new PublicEntityCallback() {

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setLoadingMore(false);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                           if (response.isSuccess()){
                               swipeToLoadLayout.setLoadingMore(false);
                               int totalPageSize = response.getEntity().getPage().getTotalPageSize();
                               if (currentPage >= totalPageSize) {
                                   swipeToLoadLayout.setLoadMoreEnabled(false);
                               }else{
                                   swipeToLoadLayout.setLoadMoreEnabled(true);
                               }
                               list.addAll(response.getEntity().getAssessList());
                               courseCommentAdapter.notifyDataSetChanged();
                           }else {
                               IToast.show(getActivity(),response.getMessage());
                           }
                        } catch (Exception e) {
                        }
                    }
                }
        );

    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @OnClick(R.id.send_message)
    public void sendMessage(){
        String content = discussEditText.getText().toString();
        discussEditText.setText("");
        if(!TextUtils.isEmpty(content)){
            if(userId != -1){
                AddCourseComment(content);
            }else{
                IToast.show(getActivity(), "请先登陆");
            }
        }else{
            IToast.show(getActivity(), "请输入内容");
        }

    }

    private void AddCourseComment(String content) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("courseAssess.courseId", String.valueOf(courseId));
        map.put("courseAssess.kpointId", String.valueOf(kpointId));
        map.put("courseAssess.content", content);
        OkHttpUtils.post().params(map).url(Address.ADD_COURSE_COMMENT).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                list.clear();
                                getCourseComment();
                                Toast.makeText(getActivity(), "评论发表成功！",Toast.LENGTH_SHORT).show();
                                IToast.show(getActivity(),"评论发表成功！");
                            }else {
                                IToast.show(getActivity(),response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );


    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getCourseComment();
    }
}


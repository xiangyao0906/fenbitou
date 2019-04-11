package com.fenbitou.wantongzaixian;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.CourseListAdapter;
import com.fenbitou.adapter.TagFlowAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.entity.PublicListEntity;
import com.fenbitou.entity.PublicListEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2017/6/29.
 * 课程
 */

public class CourseFragment extends BaseFragment {

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.course_search)
    ImageView courseSearch;
    @BindView(R.id.course_list_view)
    NoScrollListView courseListView;
    @BindView(R.id.tag_flow_layout)
    TagFlowLayout tagFlowLayout;
    @BindViews({R.id.subject_text,R.id.teacher_text,R.id.sort_text})
    List<TextView> textViewList;
    @BindViews({R.id.subject_image,R.id.teacher_image,R.id.sort_image})
    List<ImageView> imageViewList;

    private CourseListAdapter courseListAdapter;
    private List<EntityCourse> courseList;

    private TagFlowAdapter tagFlowAdapter;
    private List<EntityPublic> tagList;
    private Animation animationIn, animationOut;
    private boolean isSubjectShow, isTeacherShow, isSortShow;
    private int subjectPosition, teacherPosition, sortPosition;
    private List<EntityPublic> subjectList,teacherList,sortList;
    private int subjectId,teacherId,sortId;
    private int currentPage = 1;
    private EntityPublic allCourseEntity ;
    private List<EntityCourse> allList; //课程列表

    @Override
    protected int initContentView() {
        return R.layout.fragment_course;
    }
    @Override
    protected void initComponent() {
        allList = new ArrayList<EntityCourse>();
        courseList = new ArrayList<>();
        courseListAdapter = new CourseListAdapter(getActivity(),courseList);
        courseListView.setAdapter(courseListAdapter);
        tagList = new ArrayList<>();
        tagFlowAdapter = new TagFlowAdapter(tagList,getActivity());
        tagFlowLayout.setAdapter(tagFlowAdapter);
        animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_out_show);
        subjectList = new ArrayList<>();
        teacherList = new ArrayList<>();
        sortList = new ArrayList<>();
        allCourseEntity = new EntityPublic();
        allCourseEntity.setSubjectName("全部");
        allCourseEntity.setSubjectId(0);
        allCourseEntity.setId(0);
        allCourseEntity.setName("全部");
    }

    @Override
    protected void initData() {
        getCourseList();
    }

    private void getCourseList() {
        Map<String, String> map = new HashMap<>();
        map.put("page.currentPage", String.valueOf(currentPage));
        map.put("queryCourse.order", String.valueOf(sortId));
        map.put("queryCourse.subjectId", String.valueOf(subjectId));
        map.put("queryCourse.teacherId", String.valueOf(teacherId));
        OkHttpUtils.post().params(map).url(Address.COURSE_LIST).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onAfter(int id) {

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                swipeToLoadLayout.setRefreshing(false);
                                swipeToLoadLayout.setLoadingMore(false);
                                int totalPageSize = response.getEntity().getPage().getTotalPageSize();
                                if (courseList != null){
                                    courseList.clear();
                                }
                                allList = response.getEntity().getCourseList();
                                if (currentPage >= totalPageSize) {
                                    swipeToLoadLayout.setLoadMoreEnabled(false);
                                }else{
                                    swipeToLoadLayout.setLoadMoreEnabled(true);
                                }
                                courseList.addAll(allList);
                                courseListAdapter.notifyDataSetChanged();
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
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        courseListView.setOnItemClickListener(this);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                tagFlowLayout.startAnimation(animationOut);
                tagFlowLayout.setVisibility(View.GONE);
                cleanSelect();
                if (isSubjectShow){
                    if (position == 0){
                        textViewList.get(0).setText(R.string.major);
                    }else {
                        textViewList.get(0).setText(tagList.get(position).getSubjectName());
                    }
                    subjectPosition = position;
                    subjectId = tagList.get(position).getSubjectId();
                    isSubjectShow = false;
                }
                if (isTeacherShow){
                    if (position == 0){
                        textViewList.get(1).setText(R.string.teacher);
                    }else {
                        textViewList.get(1).setText(tagList.get(position).getName());
                    }
                    teacherPosition = position;
                    teacherId = tagList.get(position).getId();
                    isTeacherShow = false;
                }
                if (isSortShow){
                    if (position == 0){
                        textViewList.get(2).setText(R.string.sort);
                    }else {
                        textViewList.get(2).setText(tagList.get(position).getName());
                    }
                    sortPosition = position;
                    sortId = tagList.get(position).getId();
                    isSortShow = false;
                }
                getCourseList();
                return true;
            }
        });
    }

    @OnClick(R.id.course_search)
    public void search(){
        openActivity(SearchActivity.class);
    }

    @OnClick({R.id.subject_layout,R.id.teacher_layout,R.id.sort_layout})
    public void onClick(View v){
        cleanSelect();
        switch (v.getId()){
            case R.id.subject_layout: // 专业
                if (isSubjectShow){
                    tagFlowLayout.startAnimation(animationOut);
                    tagFlowLayout.setVisibility(View.GONE);
                    isSubjectShow = false;
                    return;
                }
                if (!isTeacherShow && !isSortShow){
                    tagFlowLayout.startAnimation(animationIn);
                    tagFlowLayout.setVisibility(View.VISIBLE);
                }
                textViewList.get(0).setTextColor(getResources().getColor(R.color.color_main));
                imageViewList.get(0).setBackgroundResource(R.drawable.drop_up);
                isSubjectShow = true;
                isTeacherShow = false;
                isSortShow = false;
                if (subjectList.isEmpty()) {
                    getSubjectList();
                }else {
                    upDateTag(subjectList,subjectPosition);
                }
                break;
            case R.id.teacher_layout:  //讲师
                if (isTeacherShow){
                    tagFlowLayout.startAnimation(animationOut);
                    tagFlowLayout.setVisibility(View.GONE);
                    isTeacherShow = false;
                    return;
                }
                if (!isSubjectShow && !isSortShow){
                    tagFlowLayout.startAnimation(animationIn);
                    tagFlowLayout.setVisibility(View.VISIBLE);
                }
                textViewList.get(1).setTextColor(getResources().getColor(R.color.color_main));
                imageViewList.get(1).setBackgroundResource(R.drawable.drop_up);
                isSubjectShow = false;
                isTeacherShow = true;
                isSortShow = false;
                if (teacherList.isEmpty()) {
                    getTeacherList();
                }else {
                    upDateTag(teacherList,teacherPosition);
                }
                break;
            case R.id.sort_layout: // 排序
                if (isSortShow){
                    tagFlowLayout.startAnimation(animationOut);
                    tagFlowLayout.setVisibility(View.GONE);
                    isSortShow = false;
                    return;
                }
                if (!isSubjectShow && !isTeacherShow){
                    tagFlowLayout.startAnimation(animationIn);
                    tagFlowLayout.setVisibility(View.VISIBLE);
                }
                textViewList.get(2).setTextColor(getResources().getColor(R.color.color_main));
                imageViewList.get(2).setBackgroundResource(R.drawable.drop_up);
                isSubjectShow = false;
                isTeacherShow = false;
                isSortShow = true;
                if (sortList.isEmpty()) {
                    getSortList();
                }else {
                    upDateTag(sortList,sortPosition);
                }
                break;
        }
    }

    private void getSubjectList() {
        Map<String, String> map = new HashMap<>();
        map.put("parentId", String.valueOf(subjectId));
        OkHttpUtils.post().params(map).url(Address.MAJOR_LIST).build().execute(
                new PublicListEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicListEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                subjectList.clear();
                                subjectList.add(allCourseEntity);
                                subjectList.addAll(response.getEntity());
                                upDateTag(subjectList,subjectPosition);
                            }else {
                                IToast.show(getActivity(),response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void getTeacherList() {
        OkHttpUtils.get().url(Address.COURSE_TEACHER_LIST).build().execute(
                new PublicListEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicListEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                teacherList.clear();
                                teacherList.add(allCourseEntity);
                                teacherList.addAll(response.getEntity());
                                upDateTag(teacherList,teacherPosition);
                            }else {
                                IToast.show(getActivity(),response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void getSortList() {
        sortList.clear();
        sortList.add(allCourseEntity);
        for(int i=0; i < 3;i++){
            EntityPublic entity = new EntityPublic();
            switch (i) {
                case 0:
                    entity.setName("最新");
                    entity.setId(2);
                    break;
                case 1:
                    entity.setName("热门");
                    entity.setId(1);
                    break;
                case 2:
                    entity.setName("价格");
                    entity.setId(3);
                    break;
                default:
                    break;
            }
            sortList.add(entity);
        }
        upDateTag(sortList,sortPosition);
    }


    public void cleanSelect() {
        for (TextView textView:textViewList){
            textView.setTextColor(getResources().getColor(R.color.color_67));
        }
        for (ImageView imageView:imageViewList){
            imageView.setBackgroundResource(R.drawable.drop_down);
        }
    }

    /**
     * 更新标签布局
     * @param list  数据集合
     * @param position 选择下标
     */
    public void upDateTag(List<EntityPublic> list,int position){
        tagList.clear();
        tagList.addAll(list);
        Set<Integer> set = new HashSet<>();
        set.add(position);
        tagFlowAdapter.setSelectedList(set);
        tagFlowAdapter.notifyDataChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
//        intent.setClass(getActivity(),CourseDetails96kActivity.class);
        intent.putExtra("courseId", courseList.get(i).getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        courseList.clear();
        getCourseList();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getCourseList();
    }
}

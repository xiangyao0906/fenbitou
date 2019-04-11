package com.fenbitou.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.fenbitou.adapter.CatalogueExpandableAdapter;
import com.fenbitou.adapter.CourseDirectoryAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.entity.downtListEvent;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollExpandableListView;
import com.fenbitou.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by admin on 2017/7/3.
 * 课程目录
 */

public class CourseDirectoryFragment extends BaseFragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    @BindView(R.id.grid_view)
    NoScrollGridView gridView;
    @BindView(R.id.expandable_list_view)
    NoScrollExpandableListView expandableListView;

    private List<EntityPublic> packageList,kpointList;
    private CourseDirectoryAdapter  courseDirectoryAdapter;
    private CatalogueExpandableAdapter catalogueExpandableAdapter;
    private int courseId,userId;  //课程Id,用户Id
    private EntityPublic entityPublic;
//    private CourseDetails96kActivity courseDetailsActivity;
    private boolean isok = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        courseDetailsActivity = (CourseDetails96kActivity) context;
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_course_directory;
    }

    @Override
    protected void initComponent() {
        packageList = new ArrayList<>();
        courseDirectoryAdapter = new CourseDirectoryAdapter(getActivity(),packageList);
        gridView.setAdapter(courseDirectoryAdapter);
        kpointList = new ArrayList<>();
        catalogueExpandableAdapter = new CatalogueExpandableAdapter(getActivity(),kpointList);
        expandableListView.setAdapter(catalogueExpandableAdapter);
    }

    @Override
    protected void initData() {
        userId= (int) SharedPreferencesUtils.getParam(getActivity(),"userId",-1);
        entityPublic = (EntityPublic) getArguments().getSerializable("entity");
        if (entityPublic == null) return;
        isok = entityPublic.isok();
        courseId = entityPublic.getCourse().getId();
        packageList.addAll(entityPublic.getCoursePackageList());
        courseDirectoryAdapter.notifyDataSetChanged();
        kpointList.addAll(entityPublic.getCourseKpoints());
        catalogueExpandableAdapter.notifyDataSetChanged();
        if (!kpointList.isEmpty()) expandableListView.expandGroup(0);
    }

    @Override
    protected void addListener() {
        gridView.setOnItemClickListener(this);  //课程根目录的布局
        expandableListView.setOnGroupClickListener(this);
        expandableListView.setOnChildClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        courseDirectoryAdapter.setSelectPosition(position);
        courseDirectoryAdapter.notifyDataSetChanged();
        int currentCourseId = packageList.get(position).getId();  //得到根目录的Id
        int parentId = packageList.get(position).getId();  //得到根目录的Id
        //获取目录的方法
        getCourseDetails(currentCourseId);
        EventBus.getDefault().post(new downtListEvent(parentId+""));
    }
    private void getCourseDetails(int currentCourseId) {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", String.valueOf(courseId));
        map.put("userId", String.valueOf(userId));
        map.put("currentCourseId", String.valueOf(currentCourseId));
        OkHttpUtils.post().params(map).url(Address.COURSE_DETAILS).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if(response.isSuccess()){
                            kpointList.clear();
                            kpointList.addAll(response.getEntity().getCourseKpoints());
                            catalogueExpandableAdapter.notifyDataSetChanged();
                            if (null != response.getEntity().getCourseKpoints() && response.getEntity().getCourseKpoints().size() > 0) {
                                expandableListView.expandGroup(0);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (kpointList.get(groupPosition).getType() == 0) {
            EntityPublic entityCourse = kpointList.get(groupPosition);
            catalogueExpandableAdapter.setSelectEntity(entityCourse);
            catalogueExpandableAdapter.notifyDataSetChanged();
//            courseDetailsActivity.isPlayImmediately = true;
//            courseDetailsActivity.verificationPlayVideo(entityCourse.getId());
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        EntityPublic entityCourse = kpointList.get(groupPosition).getChildKpoints().get(childPosition);
        catalogueExpandableAdapter.setSelectEntity(entityCourse);
        catalogueExpandableAdapter.notifyDataSetChanged();
//        courseDetailsActivity.isPlayImmediately = true;
//        courseDetailsActivity.verificationPlayVideo(entityCourse.getId());
        return false;
    }
}

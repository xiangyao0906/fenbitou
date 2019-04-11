package com.fenbitou.Exam.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.Exam.adapter.SubAdapter;
import com.fenbitou.Exam.constants.ExamAddress;
import com.fenbitou.Exam.entity.ListEntity;
import com.fenbitou.Exam.entity.ListEntityCallback;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author bin
 *         时间: 2016/5/31 12:50
 *         类说明:侧滑菜单的类
 */
public class ExamSlidingMenuFragment extends BaseFragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
    @BindView(R.id.sub_list)
    ExpandableListView subList;
    @BindView(R.id.null_text)
    TextView nullText;
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;
    private ProgressDialog progressDialog;  //加载数据显示的dialog
    private ListEntity listEntity;  //专业的集合
    Unbinder unbinder;
    private Message message;


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        int subjectId = listEntity.getEntity().get(groupPosition).getSubjectList().get(childPosition).getSubjectId();
        String subjectName = listEntity.getEntity().get(groupPosition).getSubjectList().get(childPosition).getSubjectName();

        SharedPreferencesUtils.setParam(getActivity(),"subjectId",subjectId);
        SharedPreferencesUtils.setParam(getActivity(),"subjectName",subjectName);

        message.what = 101;
        message.obj = subjectName;
        EventBus.getDefault().post(message);
        return false;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int initContentView() {
        return R.layout.exam_drawerlayout_fragment;
    }

    @Override
    protected void initComponent() {
        message = new Message();
        progressDialog = new ProgressDialog(getActivity());  //加载数据的dialog
    }

    @Override
    protected void initData() {
        //联网获取专业的列表
        getSubData();
    }

    /**
     * 联网获取专业的列表
     */
    private void getSubData() {
        showLoading(getActivity());
        OkHttpUtils.get().url(ExamAddress.SUBLIST_URL).build().execute(
                new ListEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        nullLayout.setVisibility(View.VISIBLE);
                        nullText.setText("无目录节点,点击刷新");
                    }

                    @Override
                    public void onResponse(ListEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                cancelLoading();
                                nullLayout.setVisibility(View.GONE);
                                listEntity = response;
                                SubAdapter adapter = new SubAdapter(getActivity(), response);
                                subList.setAdapter(adapter);
                            } else {
                                IToast.show(getActivity(), response.getMessage());
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }
                    }
                });

    }

    @Override
    protected void addListener() {
        subList.setOnGroupClickListener(this);  //父目录点击事件
        subList.setOnChildClickListener(this);  //子目录点击事件
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.null_layout)
    public void onViewClicked() {
        getSubData();
    }
}

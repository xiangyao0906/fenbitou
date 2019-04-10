package com.yizhilu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.adapter.RecordListAdapter;
import com.yizhilu.base.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by admin on 2017/6/29.
 * 记录
 */

public class RecordFragment extends BaseFragment {

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    ListView recordListView;
    @BindView(R.id.null_data_layout)
    LinearLayout nullDataLayout;
    @BindView(R.id.null_login_layout)
    LinearLayout nullLoginLayout;

    private getExitBroad exitBroad;

    private List<EntityPublic> recordList;
    private RecordListAdapter recordListAdapter;
    private int userId;
    private int currentPage = 1;

    @Override
    protected int initContentView() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initComponent() {
        recordList = new ArrayList<>();
        recordListAdapter = new RecordListAdapter(getActivity(), recordList);
        recordListView.setAdapter(recordListAdapter);
        exitBroad = new getExitBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction("exitApp");
        getActivity().registerReceiver(exitBroad, filter);
    }

    @Override
    public void onResume() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        if (userId == -1) {
            nullLoginLayout.setVisibility(View.VISIBLE);
            nullDataLayout.setVisibility(View.GONE);
        } else {
            nullDataLayout.setVisibility(View.GONE);
            nullLoginLayout.setVisibility(View.GONE);
            recordList.clear();
            currentPage = 1;
            getRecordByUserId(userId);
        }
        super.onResume();
    }


    @Override
    protected void initData() {
    }

    private void getRecordByUserId(int userid) {
        Map<String, String> map = new HashMap<>();
        map.put("page.currentPage", String.valueOf(currentPage));
        map.put("userId", String.valueOf(userid));
        ILog.i(Address.PLAY_HISTORY + "?" + map + "---------------------学习记录列表");
        showLoading(getActivity());
        OkHttpUtils.post().params(map).url(Address.PLAY_HISTORY).build().execute(
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
                            cancelLoading();
                            if (response.isSuccess()) {
                                swipeToLoadLayout.setRefreshing(false);
                                swipeToLoadLayout.setLoadingMore(false);
                                int totalPageSize = response.getEntity().getPage().getTotalPageSize();
                                if (currentPage >= totalPageSize) {
                                    swipeToLoadLayout.setLoadMoreEnabled(false);
                                } else {
                                    swipeToLoadLayout.setLoadMoreEnabled(true);
                                }
                                if (response.getEntity().getStudyList() != null) {
                                    List<EntityPublic> list = response.getEntity().getStudyList();
                                    recordList.addAll(list);
                                    recordListAdapter.notifyDataSetChanged();
                                } else {
                                    nullDataLayout.setVisibility(View.VISIBLE);
                                    nullLoginLayout.setVisibility(View.GONE);
                                }

                            } else {
                                IToast.show(getActivity(), response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

    @Override
    protected void addListener() {
        recordListView.setOnItemClickListener(this);
        recordListView.setOnCreateContextMenuListener(onCreateContextMenuListener);//长按删除
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    //长按删除
    View.OnCreateContextMenuListener onCreateContextMenuListener = new View.OnCreateContextMenuListener() {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("操作");
            menu.add(100, 0, 0, "删除");
        }

    };

    //长按删除的方法
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int selectedPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行
        int id = recordList.get(selectedPosition).getId();
        deleteRecordById(id);
        return false;
    }

    /**
     * 删除记录
     *
     * @param id
     */
    private void deleteRecordById(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", String.valueOf(id));
        ILog.i(Address.DELETE_COURSE_PLAYHISTORY + "?" + map + "---------------删除");
        OkHttpUtils.post().params(map).url(Address.DELETE_COURSE_PLAYHISTORY).build().execute(
                new PublicEntityCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                recordList.clear();
                                IToast.show(getActivity(), response.getMessage());
                                getRecordByUserId(userId);
                            } else {
                                IToast.show(getActivity(), response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), CourseDetails96kActivity.class);
//        intent.putExtra("courseId", recordList.get(i).getCourseId());
//        intent.putExtra("playHistoryTab", true);
//        intent.putExtra("HistoryKpointId", recordList.get(i).getKpointId());
//        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        recordList.clear();
        getRecordByUserId(userId);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getRecordByUserId(userId);
    }


    //  接收退出登陆的广播
    class getExitBroad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
            if ("exitApp".equals(action)) {
                nullLoginLayout.setVisibility(View.VISIBLE);
                nullDataLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exitBroad != null) getActivity().unregisterReceiver(exitBroad);
    }
}


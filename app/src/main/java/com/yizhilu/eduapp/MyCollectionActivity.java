package com.yizhilu.eduapp;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.adapter.MyCollectionAdapter;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.ItemClickListener;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.utils.TimeConstant;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;


/**
 * @author xiangyao
 * @Title: MyCollectionActivity
 * @Package com.yizhilu.myapp
 * @Description: 我的收藏
 * @date 2017/8/9 17:24
 */

public class MyCollectionActivity extends BaseActivity implements ItemClickListener.OnItemClickListener {


    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.collection_text)
    TextView collectionText;
    @BindView(R.id.collection_image_edit)
    TextView collectionImageEdit;
    @BindView(R.id.collection_delete)
    TextView collectionDelete;
    @BindView(R.id.collection_empty)
    TextView collectionEmpty;
    @BindView(R.id.collection_del_null)
    LinearLayout collectionDelNull;
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;
    private List<EntityCourse> collectList; // 收藏课程的集合
    private List<Boolean> checked; // 存放收藏是否选中的标识
    private int currentPage = 1, userId;
    private boolean temp = true, isClear; // true代表完成状态,false代表编辑状态,是否是清空
    private MyCollectionAdapter myCollectionAdapter;

    @Override
    protected int initContentView() {
        return R.layout.act_my_collection;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        collectList = new ArrayList<>();
        checked = new ArrayList<>();
        findCollection();
    }

    @Override
    protected void addListener() {
        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget, this));
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

    }


    /*
    * 查询收藏的课程
    * **/
    public void findCollection() {

        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).addParams("page.currentPage", String.valueOf(currentPage)).url(Address.COURSE_COLLECT_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    swipeToLoadLayout.setLoadingMore(false);
                    swipeToLoadLayout.setRefreshing(false);
                    PageEntity pageEntity = response
                            .getEntity().getPage();
                    if (pageEntity.getTotalPageSize() <= currentPage) {
                        swipeToLoadLayout.setLoadingMore(false);
                    } else {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                    List<EntityCourse> favouriteCourses = response.getEntity().getFavouriteCourses();
                    if (favouriteCourses != null && favouriteCourses.size() > 0) {
                        for (int i = 0; i < favouriteCourses.size(); i++) {
                            collectList.add(favouriteCourses.get(i));
                            checked.add(false);
                        }
                    }

                    if (myCollectionAdapter == null) {
                        myCollectionAdapter = new MyCollectionAdapter(MyCollectionActivity.this, collectList);
                        swipeTarget.setAdapter(myCollectionAdapter);
                    } else {
                        myCollectionAdapter.notifyDataSetChanged();
                    }


                    if (collectList.isEmpty()) {
                        collectionDelNull
                                .setVisibility(View.GONE);
                        temp = true;
                        collectionImageEdit.setText("编辑");
                        nullLayout.setVisibility(View.VISIBLE);
                    } else {
                        nullLayout.setVisibility(View.GONE);
                    }
                } else {
                }
            }
        });

    }


    @OnClick({R.id.back_layout, R.id.collection_image_edit, R.id.collection_empty, R.id.collection_del_null, R.id.collection_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.collection_image_edit:
                if (collectList.isEmpty()) {
                    return;
                }
                if (temp) {
                    collectionImageEdit.setText("完成");
                    collectionDelNull.setVisibility(View.VISIBLE);
                    myCollectionAdapter.setFlag(temp);
                    myCollectionAdapter.notifyDataSetChanged();
                    for (int i = 0; i < checked.size(); i++) {
                        checked.set(i, false);
                    }
                    temp = false;
                } else {
                    collectionImageEdit.setText("编辑");
                    collectionDelNull.setVisibility(View.GONE);
                    myCollectionAdapter.setFlag(temp);
                    myCollectionAdapter.notifyDataSetChanged();
                    temp = true;
                }
                break;
            case R.id.collection_empty:
                showTips("", true, userId);
                break;
            case R.id.collection_del_null:
                break;
            case R.id.collection_delete:

                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.get(i)) {
                        buffer.append(collectList.get(i).getFavouriteId()).append(",");
                    }
                }
                if (buffer.length() > 0) {
                    showTips(buffer.toString(), false, userId);
                } else {
                    IToast.show(MyCollectionActivity.this, "请选择要删除的收藏课程!");
                }
                break;
        }
    }


    private void showTips(final String ids, final boolean isClear, final int userId) {

        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle("提示");
        if(isClear){
            mMaterialDialog.setMessage("您确定要清空吗?");
        }else{
            mMaterialDialog.setMessage("您确定要刪除吗?");
        }
        mMaterialDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClear) {
                    cleanrFavoratList(userId);
                } else {
                        deleteByCourseId(ids);
                }
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionImageEdit.setText("编辑");
                collectionDelNull.setVisibility(View.GONE);
                myCollectionAdapter.setFlag(temp);
                myCollectionAdapter.notifyDataSetChanged();
                temp = true;
                mMaterialDialog.dismiss();
            }
        });

        mMaterialDialog.show();





    }

    private void deleteByCourseId(String ids) {

        OkHttpUtils.get().addParams("ids", ids).url(Address.DELETE_COURSE_COLLECT).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    onRefresh();
                } else {
                    IToast.show(MyCollectionActivity.this, response.getMessage());

                }
            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {
        if (temp) {
            int courseId = collectList.get(position).getCourseId();
//            Intent intent = new Intent(MyCollectionActivity.this,
//                    CourseDetails96kActivity.class);
//            intent.putExtra("courseId", courseId);
//            startActivity(intent);
        } else {
            ImageView imageView = (ImageView) view.findViewById(R.id.imagecheck);
            if (checked.get(position)) {
                imageView.setImageResource(R.drawable.collect_button);
                checked.set(position, false);
            } else {
                imageView.setImageResource(R.drawable.collect_button_select);
                checked.set(position, true);
            }
        }
    }


    /**
     * @param userId 用户Id
     * @return 返回类型
     * @Title:
     * @Description: 根据用户ID 清空用户收藏列表
     */
    public void cleanrFavoratList(int userId) {

        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).url(Address.COLLECTION_CLEAN).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {

                if (response.isSuccess()) {
                    collectionImageEdit.setText("编辑");
                    collectionDelNull.setVisibility(View.GONE);
                    nullLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        collectList.clear();
        checked.clear();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findCollection();
            }
        }, TimeConstant.REFRSHMAXTIME);

    }

    @Override
    public void onLoadMore() {
        currentPage++;
        checked.clear();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                findCollection();
            }
        }, TimeConstant.REFRSHMAXTIME);
    }

}

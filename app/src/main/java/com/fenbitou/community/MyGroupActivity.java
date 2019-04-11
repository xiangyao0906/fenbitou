package com.fenbitou.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


//社区-我的小组
public class MyGroupActivity extends BaseActivity implements View.OnClickListener {

	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private ProgressDialog progressDialog;// loading...
	private int userId;// 用户Id
	private NoScrollListView my_create_list, my_join_list, my_manage_list;// 我创建的小组列表，我加入的小组列表，我管理的小组列表
	private List<EntityPublic> groupList, joinGroupList, manageGroupList;// 我创建的小组集合，我加入的小组集合，我管理的小组集合
	private LinearLayout no_create, no_join, no_manage;// 没有创建的小组，没有加入的小组，没有管理的小组


    @Override
    protected int initContentView() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(this,"userId", -1);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        title_text = (TextView) findViewById(R.id.title_text);
        my_create_list = (NoScrollListView) findViewById(R.id.my_create_list);
        my_join_list = (NoScrollListView) findViewById(R.id.my_join_list);
        my_manage_list = (NoScrollListView) findViewById(R.id.my_manage_list);
        no_create = (LinearLayout) findViewById(R.id.no_create);
        no_join = (LinearLayout) findViewById(R.id.no_join);
        no_manage = (LinearLayout) findViewById(R.id.no_manage);
        title_text.setText("我的小组");// 设置标题
        progressDialog = new ProgressDialog(this);
        joinGroupList = new ArrayList<EntityPublic>();
        manageGroupList = new ArrayList<EntityPublic>();
        groupList = new ArrayList<EntityPublic>();
        getMyGroup(userId);// 获取我的小组
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {
        back_layout.setOnClickListener(this);
        my_create_list.setOnItemClickListener(this);
        my_join_list.setOnItemClickListener(this);
        my_manage_list.setOnItemClickListener(this);
    }





	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.my_create_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", groupList.get(arg2).getId());
			startActivity(intent);
			break;
		case R.id.my_join_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", joinGroupList.get(arg2).getGroupId());
			startActivity(intent);
			break;
		case R.id.my_manage_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", manageGroupList.get(arg2).getGroupId());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// 获取我的小组
	private void getMyGroup(int userId) {


        OkHttpUtils
                .get()
                .addParams("userId", String.valueOf(userId))
                .url(Address.MYGROUP)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                List<EntityPublic> tempGroupList = response.getEntity().getGroupList();
                                List<EntityPublic> tempJoinGroupList = response.getEntity().getJoinGroupList();
                                List<EntityPublic> tempManageGroupList = response.getEntity().getManageGroupList();
                                if (tempGroupList != null && tempGroupList.size() > 0) {
                                    no_create.setVisibility(View.VISIBLE);
                                    groupList.addAll(tempGroupList);
                                    my_create_list
                                            .setAdapter(new MyGroupAdapter(groupList, MyGroupActivity.this, "Create"));
                                }
                                if (tempJoinGroupList != null && tempJoinGroupList.size() > 0) {
                                    no_join.setVisibility(View.VISIBLE);
                                    joinGroupList.addAll(tempJoinGroupList);
                                    my_join_list.setAdapter(new MyGroupAdapter(joinGroupList, MyGroupActivity.this, null));
                                }
                                if (tempManageGroupList != null && tempManageGroupList.size() > 0) {
                                    no_manage.setVisibility(View.VISIBLE);
                                    manageGroupList.addAll(tempManageGroupList);
                                    my_manage_list
                                            .setAdapter(new MyGroupAdapter(manageGroupList, MyGroupActivity.this, null));
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });




	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                MyGroupActivity.this.finish();
                break;

            default:
                break;
        }
    }
}

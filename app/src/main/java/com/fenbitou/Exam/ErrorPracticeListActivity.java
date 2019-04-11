package com.fenbitou.Exam;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fenbitou.Exam.adapter.CapacityErrorAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.wantongzaixian.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bin
 *         时间: 2016/6/2 20:35
 *         类说明:智能错题练习列表
 */
public class ErrorPracticeListActivity extends BaseActivity implements RecordInterface {

    @BindView(R.id.side_menu)
    ImageView sideMenu;  //返回图片
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;  //返回布局
    @BindView(R.id.title)
    TextView title;  //标题
    @BindView(R.id.errorList)
    ListView errorList;  //智能做题列表
    private int[] titles, contents, models;  //标题,内容,模式
    private CapacityErrorAdapter capacityErrorAdapter;  //智能错题列表的适配器
    private Intent intent;  //意图对象

    @Override
    protected int initContentView() {
        return R.layout.activity_error_practice_list;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
        titles = new int[]{R.string.orderPractice, R.string.randomPractice, R.string.systemPractice};
        contents = new int[]{R.string.orderContent, R.string.randomContent, R.string.systemContent};
        models = new int[]{R.string.examModel, R.string.practiceModel};
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.error_practice);  //设置标题
        capacityErrorAdapter = new CapacityErrorAdapter(ErrorPracticeListActivity.this, titles, contents, models);
        capacityErrorAdapter.setRecordInterface(ErrorPracticeListActivity.this);  //绑定点击事件
        errorList.setAdapter(capacityErrorAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    @OnClick(R.id.left_layout)
    public void onViewClicked() {
        ErrorPracticeListActivity.this.finish();
    }

    @Override
    public void myClick(int position, String name) {
        intent.setClass(ErrorPracticeListActivity.this, BeginExamActivity.class);
        intent.putExtra("examName", "错题智能练习");
        if ("考试模式".equals(name)) {
            intent.putExtra("practiceType", 1);  //1为：考试模式
        } else if ("练习模式".equals(name)) {
            intent.putExtra("practiceType", 2);  //2为：练习模式
        }
        startActivity(intent);
    }

}

package com.yizhilu.Exam;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.Exam.adapter.KnowledgePointAdapter;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bin
 *         时间: 2016/6/1 11:23
 *         类说明:知识点练习页
 */
public class KnowledgePointActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
    @BindView(R.id.pointListView)
    ExpandableListView pointListView;  //知识点的列表
    @BindView(R.id.fifteen_text)
    TextView fifteenText;  //来15道
    @BindView(R.id.order_text)
    TextView orderText;  //顺序
    @BindView(R.id.back_image)
    ImageView backImage;  //返回
    private PublicEntity publicEntity;  //实体类
    private KnowledgePointAdapter pointAdapter;  //知识点列表的适配器
    private List<PublicEntity> pointList;  //知识点集合
    private int pointiId;  //知识点Id
    private Intent intent;  //意图对象
    private String itemName;

    @Override
    protected int initContentView() {
        return R.layout.activity_knowledge_point;
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void initData() {
        //获取传过来的信息
        getIntentMessage();
        intent = new Intent();  //意图对象
        pointList = publicEntity.getEntity().getPointList();
        pointAdapter = new KnowledgePointAdapter(KnowledgePointActivity.this, pointList);
        pointListView.setAdapter(pointAdapter);
    }

    @Override
    protected void addListener() {
//        backImage.setOnClickListener(this);  //返回
        pointListView.setOnGroupClickListener(this);  //父的点击事件
        pointListView.setOnChildClickListener(this);  //子的点击事件
//        fifteenText.setOnClickListener(this);  //来15道
//        orderText.setOnClickListener(this);  //顺序
    }

    /**
     * @author bin
     * 时间: 2016/6/4 10:23
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        Intent intent = getIntent();
        publicEntity = (PublicEntity) intent.getSerializableExtra("publicEntity");  //传过来的实体类
    }


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        List<PublicEntity> examPointSon = pointList.get(groupPosition).getExamPointSon();
        if (examPointSon == null || examPointSon.size() <= 0) {
            pointiId = pointList.get(groupPosition).getId();  //知识点Id

            pointAdapter.setGroupPosition(groupPosition);
            pointAdapter.notifyDataSetChanged();
            return false;
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        pointiId = pointList.get(groupPosition).getExamPointSon().get(childPosition).getId();
        itemName=pointList.get(groupPosition).getExamPointSon().get(childPosition).getName();
        pointAdapter.setGroupPosition(groupPosition);
        pointAdapter.setChildPosition(childPosition);
        pointAdapter.notifyDataSetChanged();
        return true;
    }

    @OnClick({R.id.back_image, R.id.fifteen_text, R.id.order_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image:  //返回
                KnowledgePointActivity.this.finish();
                break;
            case R.id.fifteen_text:  //来15道
                if(pointiId == 0){
                    ConstantUtils.showMsg(KnowledgePointActivity.this,"请选择知识点");
                }else{
                    intent.setClass(KnowledgePointActivity.this, BeginExamActivity.class);
                    intent.putExtra("examName","知识点练习");
                    intent.putExtra("type","15");
                    intent.putExtra("titleName",itemName);
                    intent.putExtra("pointId",pointiId);
                    startActivity(intent);
                    KnowledgePointActivity.this.finish();
                }
                break;
            case R.id.order_text:  //顺序
                if(pointiId == 0){
                    ConstantUtils.showMsg(KnowledgePointActivity.this,"请选择知识点");
                }else{
                    intent.setClass(KnowledgePointActivity.this, BeginExamActivity.class);
                    intent.putExtra("examName","知识点练习");
                    intent.putExtra("type","100");
                    intent.putExtra("pointId",pointiId);
                    startActivity(intent);
                    KnowledgePointActivity.this.finish();
                }
                break;
        }
    }
}

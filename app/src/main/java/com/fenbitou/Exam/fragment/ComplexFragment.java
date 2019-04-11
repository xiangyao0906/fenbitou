package com.fenbitou.Exam.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fenbitou.Exam.entity.PublicEntity;
import com.fenbitou.Exam.utils.HtmlImageGetter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.wantongzaixian.R;

/**
 * 作者：caibin
 * 时间：2016/6/8 11:28
 * 类说明：材料题材料的fragment
 */
public class ComplexFragment extends BaseFragment {
    private View inflate;  //总布局
    private TextView complexName;   //材料题
    private PublicEntity publicEntity;  //材料的实体

    /**
     * @author bin
     * 时间: 2016/6/4 17:54
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        Bundle arguments = getArguments();  //获取传过来的信息
        publicEntity = (PublicEntity) arguments.getSerializable("entity");  //考试的总的实体
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_complex;
    }

    @Override
    protected void initComponent() {
        complexName = (TextView) inflate.findViewById(R.id.complexName);  //材料题的材料
        Drawable defaultDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //设置材料题的材料
        complexName.setText(Html.fromHtml(publicEntity.getComplexContent(), new HtmlImageGetter(getActivity(),complexName, "/esun_msg", defaultDrawable), null));
    }

    @Override
    protected void initData() {
        //获取传过来的信息
        getIntentMessage();
    }

    @Override
    protected void addListener() {

    }
}

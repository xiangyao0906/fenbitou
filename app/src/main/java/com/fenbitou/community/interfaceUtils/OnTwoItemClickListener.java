package com.fenbitou.community.interfaceUtils;

/**
 * Created by xiangyao on 2017/8/23.
 *
 * todo 自定义的Listview的点击事件接口
 */

public interface OnTwoItemClickListener {


    //进入小组详情
    void OnItemClickListener(int position);

    //加入小组
    void OnItemJoinClickListener(int position);
}

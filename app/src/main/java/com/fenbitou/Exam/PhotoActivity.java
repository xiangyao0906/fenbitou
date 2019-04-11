package com.fenbitou.Exam;

import android.content.Intent;
import android.view.View;

import com.fenbitou.Exam.constants.ExamAddress;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author bin
 * 时间: 2016/6/28 15:36
 * 类说明:图片缩小放大的类
 */
public class PhotoActivity extends BaseActivity {
	private PhotoView photoView;
	private String url;  //图片的地址
	@Override
	protected int initContentView() {
		return R.layout.activity_photo;
	}

	@Override
	protected void initComponent() {
		getIntentMessage();
		photoView = (PhotoView) findViewById(R.id.photoView);
		GlideUtil.loadImage(this,ExamAddress.IMAGE_HOST + url,photoView);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void addListener() {
		photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				PhotoActivity.this.finish();
			}
		});
	}

	/**
	 * 获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");  //图片路径
	}

}

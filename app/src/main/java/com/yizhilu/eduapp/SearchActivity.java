package com.yizhilu.eduapp;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yizhilu.adapter.HotSearchAdapter;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.community.adapter.SearchResultsArticleAdapter;
import com.yizhilu.community.adapter.SearchResultsCourseAdapter;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class SearchActivity extends BaseActivity implements View.OnClickListener{

	private TextView tv_cancel, tv_sure;
	private ListView lv_search;
	private NoScrollListView lv_kecheng, lv_wenzhang;
	private EditText ed_search;
	private LinearLayout layout_search, layout_search_ing;
	private List<EntityCourse> myList;
	private List<EntityCourse> courseSearch;
	private List<EntityCourse> courseSearchList;
	private List<EntityCourse> articleSearch;
	private List<EntityCourse> articleSearchList;
	private LinearLayout show_course_img,show_article_img; // 返回 课程默认图片 文章默认图片

	@Override
	protected int initContentView() {
		return R.layout.activity_search;
	}

	@Override
	protected void initComponent() {
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		lv_search = (ListView) findViewById(R.id.lv_search);
		lv_kecheng = (NoScrollListView) findViewById(R.id.lv_kecheng);
		lv_wenzhang = (NoScrollListView) findViewById(R.id.lv_wenzhang);
		ed_search = (EditText) findViewById(R.id.ed_search);
		layout_search = (LinearLayout) findViewById(R.id.layout_search);
		layout_search_ing = (LinearLayout) findViewById(R.id.layout_search_ing);
		layout_search.setVisibility(View.VISIBLE);
		layout_search_ing.setVisibility(View.GONE);
		myList = new ArrayList<>();
		courseSearchList = new ArrayList<>();
		articleSearchList = new ArrayList<>();
		show_course_img = (LinearLayout) findViewById(R.id.show_course_linear);//课程默认图片
		show_article_img = (LinearLayout) findViewById(R.id.show_article_linear); //文章默认图片
		//联网获取热门搜索的课程
		getHotSearch();
	}

	@Override
	protected void initData() {

	}


	@Override
	public void addListener() {
		tv_cancel.setOnClickListener(this);
		tv_sure.setOnClickListener(this);
		lv_search.setOnItemClickListener(this);
		lv_kecheng.setOnItemClickListener(this);
		lv_wenzhang.setOnItemClickListener(this);

		layout_search_ing.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				layout_search_ing.setFocusable(true);
				layout_search_ing.setFocusableInTouchMode(true);
				layout_search_ing.requestFocus();
				return false;
			}
		});
		ed_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				tv_cancel.setVisibility(View.GONE);
				tv_sure.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(ed_search.getText().toString())) {
					tv_cancel.setVisibility(View.VISIBLE);
					tv_sure.setVisibility(View.GONE);
					layout_search.setVisibility(View.VISIBLE);
					layout_search_ing.setVisibility(View.GONE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_cancel:
				this.finish();
				break;
			case R.id.tv_sure:
				courseSearchList.clear();
				articleSearchList.clear();
				String content = ed_search.getText().toString();
				//获取搜索的课程
				getSearchCoursesResults(content);
				//获取搜索的文章
				getSearchArticleResults(content);
				layout_search.setVisibility(View.GONE);
				layout_search_ing.setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent();
		switch (parent.getId()) {
			case R.id.lv_search:
//				intent.setClass(SearchActivity.this, CourseDetails96kActivity.class);
//				intent.putExtra("courseId", myList.get(position).getCourseId());
//				startActivity(intent);
				break;
			case R.id.lv_kecheng:
//				intent.setClass(SearchActivity.this, CourseDetails96kActivity.class);
//				intent.putExtra("courseId", courseSearchList.get(position).getId());
//				startActivity(intent);
				break;
			case R.id.lv_wenzhang:
				intent.setClass(SearchActivity.this, IndustryInformationDetailsActivity.class);
				intent.putExtra("informationTitle", "搜索文章");
				intent.putExtra("entity", articleSearchList.get(position));
				startActivity(intent);
				break;
		}
	}

	/**
	 * 热门搜索
	 */
	private void getHotSearch() {
		OkHttpUtils.get().url(Address.RECOMMEND_COURSE).build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {

			}

			@Override
			public void onResponse(String response, int id) {
				try {
					JSONObject object = new JSONObject(response);
					boolean success = object.getBoolean("success");
					if (success) {
						JSONObject entityObject = object.getJSONObject("entity");
						if (entityObject.toString().contains("\"1\"")) {
							JSONArray jsonArray = entityObject.getJSONArray("1");
							if (jsonArray != null && jsonArray.length() > 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = jsonArray.getJSONObject(i);
									EntityCourse entityCourse = new Gson().fromJson(jsonObject.toString(), EntityCourse.class);
									myList.add(entityCourse);
								}
							}
						}
						lv_search.setAdapter(new HotSearchAdapter(SearchActivity.this, myList));
					}

				}catch (Exception e){

				}
			}
		});
	}

	/**
	 * 课程搜索结果
	 * @param content 关键字
	 */
	private void getSearchCoursesResults(String content) {
		OkHttpUtils.post().url(Address.SEACRH).addParams("content",content).build().execute(new PublicEntityCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {

			}

			@Override
			public void onResponse(PublicEntity response, int id) {
				try {
					if (response.isSuccess()) {
						courseSearch = response.getEntity().getCourseList();
						if (courseSearch != null && courseSearch.size() > 0) {
							for (int i = 0; i < courseSearch.size(); i++) {
								courseSearchList.add(courseSearch.get(i));
							}
							lv_kecheng.setAdapter(new SearchResultsCourseAdapter(SearchActivity.this, courseSearchList));
							show_course_img.setVisibility(View.GONE);
						}else{
							lv_kecheng.setAdapter(new SearchResultsCourseAdapter(SearchActivity.this,courseSearchList));
							show_course_img.setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}



	/**
	 * 文章搜索结果
	 * @param content 关键字
	 */
	private void getSearchArticleResults(String content) {
		OkHttpUtils.post().url(Address.SEACRH).addParams("content",content).build().execute(new PublicEntityCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {

			}

			@Override
			public void onResponse(PublicEntity response, int id) {
				try {
					if (response.isSuccess()) {
						articleSearch = response.getEntity().getArticleList();
						if (articleSearch != null && articleSearch.size() > 0) {
							for (int i = 0; i < articleSearch.size(); i++) {
								articleSearchList.add(articleSearch.get(i));
							}
							lv_wenzhang.setAdapter(new SearchResultsArticleAdapter(SearchActivity.this, articleSearchList));
							show_article_img.setVisibility(View.GONE);
						}else{
							lv_wenzhang.setAdapter(new SearchResultsArticleAdapter(SearchActivity.this, articleSearchList));
							show_article_img.setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}

}
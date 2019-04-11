package com.fenbitou.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fenbitou.Exam.constants.ExamAddress;
import com.fenbitou.Exam.entity.PublicEntity;
import com.fenbitou.Exam.entity.PublicEntityCallback;
import com.fenbitou.Exam.fragment.ExamSlidingMenuFragment;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.ConmunityMainActivity;
import com.fenbitou.wantongzaixian.LoginActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ExamActivity extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;//科目
    @BindView(R.id.classification)
    ListView classification;//考试类型
    @BindView(R.id.set_image)
    ImageView setImage;//设置
    @BindView(R.id.assessment_layout)
    LinearLayout assessment;//能力评估
    @BindView(R.id.record_layout)
    LinearLayout record;//考试记录
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.right_layout_tv)
    TextView shequ_text;  //社区
    @BindView(R.id.school_layout)
    LinearLayout school_layout;  //网校布局
    @BindView(R.id.school_text)
    TextView school_text;  //网校
    @BindView(R.id.left_text)
    TextView leftText;//科目
    //        @BindView(R.id.title)
//    TextView title;  //标题
    @BindView(R.id.error_layout)//错题记录
            LinearLayout errorLayout;
    @BindView(R.id.collection_layout)//收藏试题
            LinearLayout collectionLayout;
    @BindView(R.id.last_time_text)
    TextView lastTimeText;//为做完的题

    @BindView(R.id.head_exal)//考试文字
            LinearLayout head_exal;

    @BindView(R.id.exam_drawerlayout)
    DrawerLayout exam_DrawerLayout;
    private int[] images;//考试类型图片数组
    private String[] names;//考试类型名字数组
    private String[] contents;//考试类型内容数组
    private String[] detaileds;//考试类型详情数组
    private List<Map<String, Object>> myList;//考试类型集合
    private Intent intent;  //意图对象
    private int userId, subjectId;  //用户Id,专业的Id
    private ProgressDialog progressDialog;  //加载数据显示
    private PublicEntity notFinishPaper;  //未做完试卷的实体


    @Override
    protected int initContentView() {
        return R.layout.activity_exam;
    }

    @Override
    protected void initComponent() {
        progressDialog = new ProgressDialog(ExamActivity.this); //加载数据显示的dialog
        intent = new Intent();
        //初始化侧滑菜单
        initSlidingMenu();
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        leftText.setVisibility(View.VISIBLE);//显示科目
        rightLayout.setVisibility(View.VISIBLE);  //显示设置布局
        shequ_text.setVisibility(View.VISIBLE);
        shequ_text.setText("社区");
        school_layout.setVisibility(View.VISIBLE);
        school_text.setText("网校");
        head_exal.setVisibility(View.VISIBLE);
        images = new int[]{R.mipmap.knowledge, R.mipmap.stage,
                R.mipmap.zhenti, R.mipmap.intelligence, R.mipmap.discuss, R.mipmap.test_assembly};
        names = new String[]{"知识点练习", "阶段测试", "真题练习", "智能错题练习", "讨论题练习", "组卷练习"};
        contents = new String[]{"专业讲师，运营人员倾心打造高品质", "针对阶段学习情况，详细到考点", "覆盖历年所有真题，感受历年", "历史错题，针对性测评", "案例分析与讨论习题，详细到考点", "专业团队，科学组卷，强力打造"};
        detaileds = new String[]{"知识考点强化", "增强学习体验", "真题的", "打破疑难点", "快速击破智慧学习", "精品模考"};
        myList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("name", names[i]);
            map.put("content", contents[i]);
            map.put("detailed", detaileds[i]);
            myList.add(map);
        }
        SimpleAdapter madapter = new SimpleAdapter(this, myList,
                R.layout.item_main_type, new String[]{"image", "name", "content", "detailed"},
                new int[]{R.id.type_image, R.id.name_text, R.id.content_text, R.id.detailed_text});
        classification.setAdapter(madapter);
    }

    @Override
    protected void addListener() {
        classification.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(ExamActivity.this,"subjectId",0);
        if (subjectId != 0) {
            //获取未做完的试题
            getHttpUnFinishedData(userId, subjectId);
        }

    }


    @OnClick({R.id.last_time_text,R.id.left_layout, R.id.right_layout, R.id.school_layout, R.id.assessment_layout, R.id.record_layout, R.id.error_layout, R.id.collection_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_time_text:   //未做完试题(最近练习旁边的)
                if (notFinishPaper != null) {
                    int type = notFinishPaper.getType();
                    if (type != 2) {
                        intent.setClass(ExamActivity.this, BeginExamActivity.class);
                    } else {
                        intent.setClass(ExamActivity.this, BeginExamPaperActivity.class);
                    }
                    intent.putExtra("examName", "继续练习");
                    intent.putExtra("continueId", notFinishPaper.getId());
                    startActivity(intent);
                }
                break;
            case R.id.left_layout:  //科目按钮
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    exam_DrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.right_layout:     //社区
                openActivity(ConmunityMainActivity.class);
                this.finish();
                break;
            case R.id.school_layout:     //网校
                finish();
                break;
            case R.id.assessment_layout:    //能力评估
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (subjectId == 0) {
                        exam_DrawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        intent.setClass(this, AbilityAssessActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.record_layout:    //学习记录
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (subjectId == 0) {
                        exam_DrawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        intent.setClass(this, StudyRecordActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.error_layout:     //错题记录
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (subjectId == 0) {
                        exam_DrawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        intent.setClass(this, WrongRecordActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.collection_layout:    //收藏试题
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (subjectId == 0) {
                        exam_DrawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        intent.setClass(this, CollectRecordActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }




    /**
     * listview点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (parent.getId()) {
            case R.id.classification: //首页分类
                switch (position) {
                    case 0:  //知识点练习
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                //联网获取知识点的方法
                                getKnowledgePoint(userId, subjectId);
                            }
                        }
                        break;
                    case 1:  //阶段测试
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                Log.i("exam", "跳转阶段测试");
                                intent.setClass(ExamActivity.this, StageCommentActivity.class);
                                intent.putExtra("examType", 2);
                                intent.putExtra("examName", "阶段测试"); // 阶段测试
                                startActivity(intent);
                            }
                        }
                        break;
                    case 2:  //真题练习
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                intent.setClass(ExamActivity.this, StageCommentActivity.class);
                                intent.putExtra("examType", 3);
                                intent.putExtra("examName", "真题练习"); // 真题
                                startActivity(intent);
                            }
                        }
                        break;
                    case 3:  //智能错题练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                intent.setClass(ExamActivity.this, ErrorPracticeListActivity.class);
                                startActivity(intent);
                            }
                        }
                        break;
                    case 4:  //讨论题练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                // 联网获取论述题自测的题数
                                getDiscussNum(userId, subjectId);
                            }
                        }
                        break;
                    case 5:  //组卷练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                                exam_DrawerLayout.openDrawer(Gravity.LEFT);
                            } else {
                                // 弹出组卷的对话框
                                intent.setClass(ExamActivity.this, AssemblyExamActivity.class);
                                startActivity(intent);
                            }
                        }
                        break;
                }
                break;
        }

    }

    /**
     * @author bin
     * 时间: 2016/6/4 10:20
     * 方法说明:获取知识点练习的数据
     */
    private void getKnowledgePoint(int userId, int subId) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.KNOWLEDGEPOINT_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            intent.setClass(ExamActivity.this, KnowledgePointActivity.class);
                            intent.putExtra("publicEntity", response);
                            startActivity(intent);
                        } else {
                            IToast.show(ExamActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * @author 杨财宾 时间:2015-9-1 上午9:11:13 方法说明:练完获取论述题自测的题数
     */
    private void getDiscussNum(int userId, int subId) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.DIACUSSTESTNUM_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String message = object.getString("message");
                        boolean success = object.getBoolean("success");
                        if (success) {
                            cancelLoading();
                            String num = object.getString("entity");
                            intent.setClass(ExamActivity.this, DiscussActivity.class);
                            intent.putExtra("num", num);
                            startActivity(intent);
                        } else {

                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 联网获取未完成的试题
     */
    public void getHttpUnFinishedData(int userId, int subjectId) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subjectId));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.UNFINISHED_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            PublicEntity entity = response.getEntity();
                            if (entity != null && entity.getRecentlyNotFinishPaper() != null) {
                                notFinishPaper = entity.getRecentlyNotFinishPaper();
                                lastTimeText.setText(entity.getRecentlyNotFinishPaper().getPaperName());
                            } else {
                                lastTimeText.setText("");
                            }
                        } else {
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSlidingMenuEvent(Message msg) {
        if (msg != null && msg.what == 101) {
            leftText.setText((String) msg.obj); // 设置选中的专业的名字
            exam_DrawerLayout.closeDrawer(Gravity.LEFT);
            subjectId= (int) SharedPreferencesUtils.getParam(this,"subjectId",0);

            // 获取未做完的试题
            getHttpUnFinishedData(userId, subjectId);
        }
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * @author bin
     * 时间: 2016/5/31 17:32
     * 方法说明:初始化侧滑菜单方法
     */
    public void initSlidingMenu() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.exam_drawer_framelayout,
                new ExamSlidingMenuFragment());
        transaction.commit();
    }
}

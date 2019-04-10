package com.yizhilu.eduapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;

import static com.yizhilu.eduapp.R.id.end_time;
import static com.yizhilu.eduapp.R.id.end_time_zhuan;

/**
 * Created by bishuang on 2017/8/30.
 */

public class AcreateLiveActivity extends BaseActivity implements View.OnClickListener{

    private EditText identity_id,identity_pwd,course_title,start_time_zhuan
            ,end_date_zhuan,pre_enter_time,max_users,type;
    private Button creat_live;
    private String encodedURL;
    private TextView start_date,end_date;
    private String str;
    private String ss;

    @Override
    protected int initContentView() {
        return R.layout.act_creat_live;
    }

    @Override
    protected void initComponent() {
        identity_id = (EditText) findViewById(R.id.identity_id);
        identity_pwd = (EditText) findViewById(R.id.identity_pwd);
        course_title = (EditText) findViewById(R.id.course_title);
        start_time_zhuan = (EditText) findViewById(R.id.start_time_zhuan);
        end_date_zhuan = (EditText) findViewById(end_time_zhuan);
        creat_live = (Button) findViewById(R.id.creat_live);
        pre_enter_time = (EditText) findViewById(R.id.pre_enter_time);
        max_users = (EditText) findViewById(R.id.max_users);
        type = (EditText) findViewById(R.id.type);
        start_date = (TextView) findViewById(R.id.start_date);
//        statr_time = (TextView) findViewById(R.id.start_time);
        end_date = (TextView) findViewById(R.id.end_date);
//        end_time = (TextView) findViewById(end_time);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(System.currentTimeMillis());
//        str = formatter.format(date);
//        start_date.setText(str);
//        end_date.setText(str);
//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        ss = df.format(new Date());
//        statr_time.setText(ss);
//        end_time.setText(ss);

//        String dateTime = str;
//        Calendar calendar = Calendar.getInstance();
//        try {
//            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Long second = calendar.getTimeInMillis()/1000;
//        start_time_zhuan.setText(second+"");
    }

    @Override
    protected void initData() {
//        start_date.setOnClickListener(this);
//        statr_time.setOnClickListener(this);
//        end_date.setOnClickListener(this);
//        end_time.setOnClickListener(this);
    }

    @Override
    protected void addListener() {
        creat_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id =  identity_id.getText().toString();
                String pwd = identity_pwd.getText().toString();
                String title = course_title.getText().toString();
                String start_t = start_date.getText().toString();
                String end_t = end_date.getText().toString();
                String pre_time = pre_enter_time.getText().toString();
                String max_num =  max_users.getText().toString();
                String stuta = type.getText().toString();

//                String str = new String(paramString.getBytes(), "UTF-8");
//                String str = new String(pwd,"UTF-8");
//                try {
//                    encodedURL = URLEncoder.encode(pwd, "UTF-8");
//                    encodedURL = URLEncoder.encode(encodedURL, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                    Calendar calendar1 = Calendar.getInstance();
                    try {
                        calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start_t));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String second_start = String.valueOf(calendar1.getTimeInMillis()/1000);
                start_time_zhuan.setText(second_start);
                    Calendar calendar2 = Calendar.getInstance();
                    try {
                        calendar2.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_t));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String second_end = String.valueOf(calendar2.getTimeInMillis()/1000);
                end_date_zhuan.setText(second_end);
                initOk(id,pwd,title,second_start,second_end,stuta,max_num,pre_time);
            }
        });
    }


    private void initOk(String id, String pwd, String title, String start_t, String end_t,String type,String max_num,String pre_time) {

//        Map<String,String> map = new HashMap<>();
        Log.i("logok",pwd+"---------------url转");
//        map.put("identity_id",id);
//        map.put("identity_pwd",pwd);
//        map.put("title",title);
//        map.put("start_time",start_t);
//        map.put("end_time",end_t);
//        map.put("type",type);
//        map.put("max_users",max_num);
//        map.put("pre_enter_time",pre_time);
        OkHttpUtils.get().addParams("identity_id",id).addParams("identity_pwd",pwd)
                .addParams("title",title).addParams("start_time",start_t)
                .addParams("end_time",end_t).addParams("type",type)
                .addParams("max_users",max_num).addParams("pre_enter_time",pre_time)
                .url("http://admin.svedio.cn/common/room/create").build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {

            }
        });
//        OkHttpUtils.post().params(map).url("http://admin.svedio.cn/common/room/create").build().execute(new PublicEntityCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(PublicEntity response, int id) {
//
//            }
//        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_date:
                setDateDialog("start_date");
                break;
            case R.id.start_time:
                setTimeDialog("start_time");
                break;
            case R.id.end_date:
                setDateDialog("end_date");
                break;
            case end_time:
                setTimeDialog("end_time");
                break;
        }
    }
   private int startYear,statrMonth,startDay,startHoure,startMinute;

    public void setDateDialog(final String type){
        String[] ary = str.split("-");//调用API方法按照逗号分隔字符串

        for(String item: ary){
            System.out.println(item);
        }
        int ssyear = Integer.parseInt(ary[0]);
        int ssmonth = Integer.parseInt(ary[1]);
        int ssday = Integer.parseInt(ary[2]);
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startYear = year;
                statrMonth = monthOfYear+1;
                startDay = dayOfMonth;
                if (type.equals("start_date")){
                    Log.i("shuang",year+"-----"+monthOfYear+"-----"+dayOfMonth+"---------开始日期");
                    start_date.setText(startYear+"-"+statrMonth+"-"+startDay);

//                    String dateTime = (String) start_date.getText()+" "+statr_time.getText();
//                    Calendar calendar = Calendar.getInstance();
//                    try {
//                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Long second = calendar.getTimeInMillis()/1000;
//                    start_time_zhuan.setText(second+"");
//                    Log.i("shuang",second+"------------------start_date_zhuan");

                }else{
                    Log.i("shuang",year+"-----"+monthOfYear+"-----"+dayOfMonth+"---------结束日期");
                    end_date.setText(startYear+"-"+statrMonth+"-"+startDay);

//                    String dateTime = (String) end_date.getText()+" "+end_time.getText();
//                    Calendar calendar = Calendar.getInstance();
//                    try {
//                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Long second = calendar.getTimeInMillis()/1000;
//                    end_date_zhuan.setText(second+"");
//                    Log.i("shuang",second+"------------------end_date_zhuan");
                }

            }
        }, 2017, 8, 31).show();
    }

    public void setTimeDialog(final String type){
            String[] ss = str.split(":");//调用API方法按照逗号分隔字符串
//
//            for(String item: ary){
//                System.out.println(item);
//            }
            int sshour = Integer.parseInt(ss[0]);
            int ssminute = Integer.parseInt(ss[1]);
        Log.i("ceshi",sshour+"-----"+ssminute);
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHoure = hourOfDay;
                startMinute = minute;
                if ("start_time".equals(type)){
                    Log.i("shuang",hourOfDay+"-----"+minute+"---------开始时间");
//                    statr_time.setText(startHoure+":"+startMinute+":00");
//
//                    String dateTime = (String) start_date.getText()+" "+statr_time.getText();
//                    Calendar calendar = Calendar.getInstance();
//                    try {
//                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Long second = calendar.getTimeInMillis()/1000;
//                    start_time_zhuan.setText(second+"");
//                    Log.i("shuang",second+"------------------start_time_zhuan");

                }else{
                    Log.i("shuang",hourOfDay+"-----"+minute+"---------结束时间");
//                    end_time.setText(startHoure+":"+startMinute+":00");
//
//                    String dateTime = (String) end_date.getText()+" "+end_time.getText();
//                    Calendar calendar = Calendar.getInstance();
//                    try {
//                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Long second = calendar.getTimeInMillis()/1000;
//                    end_date_zhuan.setText(second+"");
//                    Log.i("shuang",second+"------------------end_time_zhuan");
                }
            }
        }, sshour, ssminute, true).show();
    }
}

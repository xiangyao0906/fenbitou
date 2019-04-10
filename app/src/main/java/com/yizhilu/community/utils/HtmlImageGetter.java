package com.yizhilu.community.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.yizhilu.Exam.utils.FileUtil;
import com.yizhilu.Exam.utils.NetWork;

import java.io.InputStream;
import java.text.DecimalFormat;

public class HtmlImageGetter implements ImageGetter {
    private TextView _htmlText;
    private String _imgPath;
    private Drawable _defaultDrawable;
    private Context context;
    private boolean isNull = false;

    public HtmlImageGetter(Context context, TextView htmlText, String imgPath, Drawable defaultDrawable, boolean isNull) {
        this.context = context;
        _htmlText = htmlText;
        _imgPath = imgPath;
        _defaultDrawable = defaultDrawable;
        this.isNull = isNull;
    }

    @Override
    public Drawable getDrawable(String imgUrl) {
        Log.i("lala", imgUrl);
        String imgKey = String.valueOf(imgUrl.hashCode());
        String path = Environment.getExternalStorageDirectory() + _imgPath;
        FileUtil.createPath(path);
        String[] ss = imgUrl.split("\\.");
        String imgX = ss[ss.length - 1];
        imgKey = path + "/" + imgKey + "." + imgX;
        if (FileUtil.exists(imgKey)) {
            Drawable drawable = FileUtil.getImageDrawable(imgKey);
            if (drawable != null) {

                if (isNull) {
                    drawable.setBounds(0, 0, 0, 0);
                    return drawable;
                }
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int widthPixels = displayMetrics.widthPixels - 200;

                int intrinsicHeight = drawable.getIntrinsicHeight();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                DecimalFormat df = new DecimalFormat("0.000");//设置保留位数
                String p = df.format((float) widthPixels / intrinsicWidth);
                float pixel = Float.valueOf(p);
                int width;
                int height;
                if (pixel > 1) {
                    width = (int) (intrinsicWidth * pixel);
                    height = (int) (intrinsicHeight * pixel);
                } else {
                    width = intrinsicWidth;
                    height = intrinsicHeight;
                }
                drawable.setBounds(0, 0, width,
                        height);
                return drawable;
            }
        }
        URLDrawable urlDrawable = new URLDrawable(_defaultDrawable);
        new AsyncThread(urlDrawable).execute(imgKey, imgUrl);
        return urlDrawable;
    }

    private class AsyncThread extends AsyncTask<String, Integer, Drawable> {
        private String imgKey;
        private URLDrawable _drawable;

        public AsyncThread(URLDrawable drawable) {
            _drawable = drawable;
        }

        @Override
        protected Drawable doInBackground(String... strings) {
            imgKey = strings[0];
            InputStream inps = NetWork.getInputStream(strings[1]);
            if (inps == null)
                return _defaultDrawable;
            FileUtil.saveFile(imgKey, inps);
            Drawable drawable = Drawable.createFromPath(imgKey);
            return drawable;
        }

        public void onProgressUpdate(Integer... value) {
        }

        /**
         * 网络获取图片(resul可能为null)
         */
        @Override
        protected void onPostExecute(Drawable result) {

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int widthPixels = displayMetrics.widthPixels - 200;

            if (result != null) {
                int intrinsicHeight = result.getIntrinsicHeight();
                int intrinsicWidth = result.getIntrinsicWidth();
                DecimalFormat df = new DecimalFormat("0.000");//设置保留位数
                String p = df.format((float) widthPixels / intrinsicWidth);
                float pixel = Float.valueOf(p);
                int width;
                int height;
                if (pixel > 1) {
                    width = (int) (intrinsicWidth * pixel);
                    height = (int) (intrinsicHeight * pixel);
                } else {
                    width = intrinsicWidth;
                    height = intrinsicHeight;
                }
                result.setBounds(0, 0, width, height);
                if (result != null) {
                    _drawable.setDrawable(result);
                } else {
                    _drawable.setDrawable(_defaultDrawable);
                }
            }
            _htmlText.setText(_htmlText.getText());
        }
    }


    public class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        @SuppressWarnings("deprecation")
        public URLDrawable(Drawable defaultDraw) {
            setDrawable(defaultDraw);
        }

        private void setDrawable(Drawable ndrawable) {
            drawable = ndrawable;
        }

        @Override
        public void draw(Canvas canvas) {
            drawable.draw(canvas);
        }
    }

}

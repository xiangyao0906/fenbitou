package com.fenbitou.utils;

import android.text.InputFilter;
import android.text.Spanned;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author xiangyao
 */
public class EmojiFilter implements InputFilter {

    Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    public EmojiFilter() {
        super();
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart,
                               int dend) {
        Matcher matcher = emoji.matcher(charSequence);
        if (!matcher.find()) {
            return null;
        } else {
            IToast.show("只能输入汉字,英文，数字");
            return "";
        }
    }
}


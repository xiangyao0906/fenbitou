package com.fenbitou.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则验证工具
 */
public class ValidateUtil {
    // wi=2(n-1)(mod11)
    static int[] wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

    // verifydigit
    static int[] vi = {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    static int[] ai = new int[18];

    /**
     * 验证是否是正确的邮箱格式
     *
     * @param email
     * @return true表示是正确的邮箱格式, false表示不是正确邮箱格式
     */
    public static boolean isEmail(String email) {
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线、点、中横线
        String regular = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        }
        return flag;
    }

    public static boolean isPassword(String password) {
        String regular = "^[A-Za-z0-9]$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!TextUtils.isEmpty(password)) {
            Matcher matcher = pattern.matcher(password);
            flag = matcher.matches();
        }
        return flag;
    }

    // ^[a-zA-Z0-9_\u4e00-\u9fa5]+$
    // 判断昵称是否有汉字、英文、数字、‘_’和‘-’组成
    public static boolean isNickname(String nickname) {
        String regular = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5-]+$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!TextUtils.isEmpty(nickname)) {
            Matcher matcher = pattern.matcher(nickname);
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 验证是否是手机号格式 该方法还不是很严谨,只是可以简单验证
     *
     * @param mobile
     * @return true表示是正确的手机号格式, false表示不是正确的手机号格式
     */
    public static boolean isMobile(String mobile) {
        String regular = "^(13[0-9]|14[5,7,9]|15[0-9]|16[6]|17[0-8]|18[0-9]|19[8,9])[0-9]{8}$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (mobile != null) {
            Matcher matcher = pattern.matcher(mobile);
            flag = matcher.matches();
        }
        return flag;
    }


    /**
     * 手机号脱敏处理  （中间4位加 *）
     *
     * @param mobile
     */
    public static String hideStar(String mobile) {

        if (TextUtils.isEmpty(mobile) || !ValidateUtil.isMobile(mobile)) return "";

        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

        return mobile;
    }


    /**
     * 验证是否是整数
     *
     * @param str
     */
    public static boolean isInteger(String str) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("\\d*$");
        // 字符串不为空;
        if (str.length() > 0) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches() == true) {
                flag = true;
                // 除去以0开头的情况;
                if (str.length() > 1) {
                    if ((str.charAt(0) == '0')) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 严格验证身份证号的方法，15位、18位均可
     *
     * @param idcard
     */
    public static boolean Verify(String idcard) {
        if (idcard.length() == 15) {
            idcard = uptoeighteen(idcard);
        }
        if (idcard.length() != 18) {
            return false;
        }
        String verify = idcard.substring(17, 18);
        if (verify.equalsIgnoreCase(getVerify(idcard))) {
            return true;
        }
        return false;
    }

    // getverify
    private static String getVerify(String eightcardid) {
        int remaining = 0;
        if (eightcardid.length() == 18) {
            eightcardid = eightcardid.substring(0, 17);
        }
        if (eightcardid.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = eightcardid.substring(i, i + 1);
                try {
                    ai[i] = Integer.parseInt(k);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
            for (int i = 0; i < 17; i++) {
                sum = sum + wi[i] * ai[i];
            }
            remaining = sum % 11;
        }

        return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
    }

    // 15updateto18
    private static String uptoeighteen(String fifteencardid) {
        String eightcardid = fifteencardid.substring(0, 6);
        eightcardid = eightcardid + "19";
        eightcardid = eightcardid + fifteencardid.substring(6, 15);
        eightcardid = eightcardid + getVerify(eightcardid);
        return eightcardid;
    }

    public static boolean valiID(String id) {
        if (id != null) {
            String isIDCard = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
            Pattern p = Pattern.compile(isIDCard);
            return p.matcher(id).find();
        }
        return false;
    }

    public static boolean valiDigit(String id) {
        if (id != null) {
            String isIDCard = "^\\d+$";
            Pattern p = Pattern.compile(isIDCard);
            return p.matcher(id).find();
        }
        return false;
    }

    public static boolean valiName(String name) {
        if (name != null) {
            if (isEnglishName(name)) {
                return true;
            } else if (isChineseName(name)) {
                return true;
            }
        }
        return false;

    }

    private static boolean isEnglishName(String name) {
        if (name != null) {
            String isIDCard = "^[a-zA-Z]+[/]{1}[a-zA-Z]+$";
            Pattern p = Pattern.compile(isIDCard);
            return p.matcher(name).find();
        }
        return false;
    }

    private static boolean isChineseName(String name) {

        for (int i = 0; i < name.length(); i++) {
            boolean b = isChinese(name.charAt(i));
            if (!b)
                return false;
        }
        return true;
    }

    public static boolean isChinese(char ch) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * @author bin 修改人: 时间:2015-12-3 上午9:36:43 方法说明:验证输入的内容是否是数字
     */
    public static boolean isNumbers(String text) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @author bin 修改人: 时间:2015-12-4 上午11:30:27 方法说明:验证输入的内容必须是字母和数字
     */
    public static boolean isNumberAndLetter(String text) {
        Pattern p = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @author bin 修改人: 时间:2015-12-4 上午11:30:27 方法说明:验证输入的内容必须是字母或数字
     */
    public static boolean isNumberOrLetter(String text) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        }
        return false;

    }

    /**
     * 验证金额 两位小数
     *
     * @param str
     * @return
     */
    public static boolean isPrice(String str) {
        Pattern pattern = Pattern.compile("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$");
        Matcher match = pattern.matcher(str);
        if (match.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}

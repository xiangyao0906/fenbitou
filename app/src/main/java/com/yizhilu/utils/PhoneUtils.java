package com.yizhilu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author bin 修改人: 时间:2015-12-17 下午3:36:33 类说明:手机信息的工具类
 */
public class PhoneUtils {
    private Context context;
    private TelephonyManager mTm;
    private WindowManager windowManager;  //窗口管理

    public PhoneUtils(Context context) {
        super();
        this.context = context;
        mTm = (TelephonyManager) this.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        windowManager = (WindowManager) this.context
                .getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * @author bin 修改人: 时间:2015-12-17 下午3:47:13 方法说明:获取手机型号
     */
    public String getPhoneModel() {
        return android.os.Build.MODEL; // 手机型号
    }

    /**
     * @author bin 修改人: 时间:2015-12-17 下午3:48:00 方法说明:获取手机型号
     */
    public String getPhoneBrand() {
        return android.os.Build.BRAND;// 手机品牌
    }

    /**
     * @author bin 修改人: 时间:2015-12-17 下午3:49:09 方法说明:获取手机Imei号
     */
    public String getPhoneImei() {
        return mTm.getDeviceId();
    }

    /**
     * @author bin 修改人: 时间:2015-12-17 下午3:50:02 方法说明:获取手机Imsi号
     */
    public String getPhoneImsi() {
        return mTm.getSubscriberId();
    }

    /**
     * @author bin 修改人: 时间:2015-12-17 下午3:51:15 方法说明:获取手机号的方法
     */
    public String getPhoneNumber() {
        return mTm.getLine1Number();
    }

    /**
     * @author bin
     * 修改人:
     * 时间:2015-12-28 下午5:30:48
     * 方法说明:获取手机的IP
     */
    public static String GetHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取手机IP
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * @author bin
     * 修改人:
     * 时间:2016-1-25 下午2:02:13
     * 方法说明:获取手机屏幕的宽度
     */
    public int getPhoneWidth() {
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * @author bin
     * 修改人:
     * 时间:2016-1-25 下午2:03:08
     * 方法说明:获取屏幕的高度
     */
    public int getPhoneHeight() {
        return windowManager.getDefaultDisplay().getHeight();
    }

    public String getPhoneSize() {
        return getPhoneWidth() + "*" + getPhoneHeight();
    }

    /**
     * @author bin
     * 修改人:
     * 时间:2016-1-28 下午2:03:04
     * 方法说明:保留一位小数的方法
     * position:保留的位数
     * string:处理的支付串
     */
    public static String getRetainOneDecimal(int position, String string) {
        BigDecimal bigDecimal = new BigDecimal(string);
        BigDecimal setScale = bigDecimal.setScale(position, BigDecimal.ROUND_HALF_UP);
        return setScale.toString();
    }

}

package com.mellivora.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.UUID;

public class DeviceIDUtil {

    private static SharedPreferences getCacheSp(Context context){
        String spFileName = context.getPackageName() + "_preferences";
        return context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
    }

    /**
     * 判断是否是无效ID
     *
     * @param id 是否以0开头以及结尾，以及为空
     * @return true无效
     */
    public static boolean invalidId(String id) {
        return TextUtils.isEmpty(id) || (id.startsWith("00000") && id.endsWith("00000"));
    }

    /**
     * 获得独一无二的Psuedo ID
     *
     * @return ID
     */
    public static String getUniquePsuedoID() {
        String serial;
        String m_szDevIDShort = "25" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            //API>=9 使用serial号
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial_mmc"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        String uid = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        return uid;
    }

    /**
     * 获取设备号的唯一码，订单系统使用
     */
    public static String getUniqueId(Context context) {
        SharedPreferences sp = getCacheSp(context);
        String SP_KEY = "UNIQUE_ID";
        String uniqueId = sp.getString(SP_KEY, null);
        if (invalidId(uniqueId)){
            uniqueId = getDeviceId(context);
        }
        if (invalidId(uniqueId)){
            uniqueId = getAndroidId(context);
        }
        if (invalidId(uniqueId)) {
            uniqueId = getUniquePsuedoID();
        }
        if (invalidId(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
        }
        sp.edit().putString(SP_KEY, uniqueId).apply();
        return uniqueId;
    }

    public static String getAndroidId(Context context) {
        SharedPreferences sp = getCacheSp(context);
        String SP_KEY = "Android_ID";
        String androidInfo = sp.getString(SP_KEY, null);
        if (androidInfo == null) {
            try {
                androidInfo = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if(androidInfo != null){
                    sp.edit().putString(SP_KEY, androidInfo).apply();
                }
            } catch (Exception ignored) {
            }
        }
        return androidInfo;
    }

    /**
     * 获取手机的IMEI值
     */
    public static String getDeviceId(Context context) {
        SharedPreferences sp = getCacheSp(context);
        String SP_KEY = "IMEI_ID";
        String imei = sp.getString(SP_KEY, null);
        if (imei == null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                imei = tm.getDeviceId();
            } catch (Exception ignored) {
            }
        }
        return imei;
    }


}

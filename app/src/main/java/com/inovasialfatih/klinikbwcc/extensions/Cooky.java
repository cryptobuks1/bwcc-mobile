package com.inovasialfatih.klinikbwcc.extensions;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 07/12/2017.
 */

public class Cooky {
    private static String PREF_NAME = "TS_PREF";

    public static String USER_ID = "USER_ID";
    public static String TOKEN = "TOKEN";
    public static String FCM_TOKEN = "FCM_TOKEN";
    public static String NAME = "NAME";
    public static String PHONE_NUMBER = "PHONE_NUMBER";
    public static String EMAIL = "EMAIL";
    public static String IDENTITY_NUMBER = "IDENTITY_NUMBER";
    public static String NETWORK_ID = "NETWORK_ID";
    public static String NETWORK_NAME = "NETWORK_NAME";
    public static String PROFILE_PICTURE = "PROFILE_PICTURE";
    public static String BIO = "BIO";
    public static String LINK_CODE = "LINK_CODE";
    public static String IS_CALEG = "IS_CALEG";
    public static String RESOURCE_VERSION = "RESOURCE_VERSION";
    public static String PROFILE_CHECKED_DATE = "PROFILE_CHECKED_DATE";
    public static String LAST_POS_LATITUDE = "LAST_POS_LATITUDE";
    public static String LAST_POS_LONGITUDE = "LAST_POS_LONGITUDE";
    public static String LAST_POS_DATE = "LAST_POS_DATE";
    public static String POS_LATITUDE = "POS_LATITUDE";
    public static String POS_LONGITUDE = "POS_LONGITUDE";

    private SharedPreferences pref;
    public Cooky(Context context)
    {
        pref = context.getSharedPreferences(PREF_NAME, 0);
    }

    public String token(){
        return pref.getString(TOKEN, "");
    }

    public String getString(String name){
        return pref.getString(name, "");
    }

    public Integer getInt(String name){
        return pref.getInt(name, 0);
    }

    public Boolean getBoolean(String name){
        return pref.getBoolean(name, true);
    }

    public Double getDouble(String name){
        return Double.longBitsToDouble(pref.getLong(name, Double.doubleToLongBits(0)));
    }

    public Long getLong(String name){
        return pref.getLong(name, 0);
    }

    public void addString(String name, String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public void addInt(String name, int value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public void addBoolean(String name, boolean value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public void addDouble(String name, Double value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(name, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public void addLong(String name, long value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public void clearAll() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}

package com.datainfosys.myshops.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by developer on 11/1/16.
 */
public class Constants {
    static String PREF_KEY= "app_preferences";
    static String PREF_KEY_USER_TOKEN= "pref_user_token";
    public static SharedPreferences getPreferences(Context mContext)
    {
        return mContext.getSharedPreferences(PREF_KEY, mContext.MODE_PRIVATE);
    }

    public static String getUserToken(Context context)
    {
        SharedPreferences preferences= getPreferences(context);
        return preferences.getString(PREF_KEY_USER_TOKEN, null);
    }

    public static void setUserToken(Context context, String userToken)
    {
        SharedPreferences preferences= getPreferences(context);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString(PREF_KEY_USER_TOKEN, userToken);
        editor.commit();
    }
}

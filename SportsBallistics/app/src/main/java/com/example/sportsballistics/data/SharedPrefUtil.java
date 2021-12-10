package com.example.sportsballistics.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sportsballistics.AppSystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;

public class SharedPrefUtil<T>
{

    public static final String USER_SESSION = "sp_user_session";

    public static final String IS_FCM_TOKEN_STALE = "sp_is_stale_token";
    public static final String LAST_LOCALE = "sp_locale_updated";
    public static final String PUSH_TOKEN = "sp_fcm_token";
    public static final String IS_FRESH_LOGIN = "sp_is_fresh_login";

    public static final String USER_HOME_LOCATION = "sp_home_location";
    public static final String USER_WORK_LOCATION = "sp_work_location";

    public static final String DEFAULT_PRICES = "sp_default_prices";

    public static final String CANCELLATION_REASONS = "sp_cancellation_reasons";
    public static final String RATING_REASONS = "sp_rating_reasons";
    public static final String IS_RATED = "is_rated";

    public static final String IS_RUNNING_FIRST_TIME = "sp_running_first_time";
    public static final String IS_ON_BOARDING_SHOWED = "sp_is_on_boarding_showed";
    public static final String IS_COACH_MARK_ONE_SHOWN = "sp_coachmark_one_shown";
    public static final String IS_COACH_MARK_TWO_SHOWN = "sp_coachmark_two_shown";
    public static final String IS_BIRTHDAY_SHOWN = "sp_birthday_shown";
    public static final String IS_PAYMENT_COACH_MARKS_SHOWN = "sp_payment_coachmarks_shown";
    public static final String IS_WHATS_NEW_MARKS_SHOWN = "sp_whats_new_shown";
    public static final String IS_PS_RATING_DONE = "sp_ps_rating_done";
    public static final String SHOULD_SHOW_GESTURE_MESSAGE = "sp_should_show_geture_message";
    public static final String SP_BUILD_FLAVOR = "sp_build_flavor";


    public static final String RECENT_ADDRESSES = "sp_recent_addresses";

    public static final String SYNC_TIME_BOOKING = "sp_sync_time_booking";
    public static final String SYNC_TIME_FARE = "sp_sync_time_fare";
    public static final String SYNC_TIME_CANCELLATION_REASONS = "sp_sync_time_cancellation_reasons";
    public static final String SYNC_TIME_RATING_REASONS = "sp_sync_time_rating_reasons";
    public static final String SYNC_TIME_PROFILE = "sp_sync_time_profile";
    public static final String SYNC_TIME_COMPLAINTS = "sp_sync_time_complaints";
    public static final String SYNC_TIME_PAYMENT_METHODS = "sp_sync_time_payment_methods";
    public static final String SP_COOKIES = "SP_COOKIES";

    public static final String SCAN_N_PAY_HELP_SHOWN = "sp_scan_n_pay_help_shown";
    public static final String IS_PAYMENT_DELETION_HELP_SHOWN = "sp_payment_method_deletion_help_shown";
    public static final String LAST_RESET_SYNC_TIME_DONE = "sp_reset_sync_time_done_for_version";
    public static final String RESET_SYNC_TIME_DONE = "sp_reset_sync_time_done_for_version_2";

    public static final String IS_SHARE_TOOL_TIP_SHOWN = "sp_share_tool_tip_shown";

    public static final String CURRENT_VERSION_CODE = "current_version_code";
    public static final String IS_APP_RUNNING_FIRST_TIME_AFTER_UPDATE = "is_app_running_first_time_after_update";

    public static final String SP_OFFSET_LOCAL_TO_LIVE_CLOCK = "sp_offset_local_to_live_clock";

    public static final String SP_IS_BOOKING_OTP_PERMISSION_ASKED = "sp_is_booking_otp_permission_asked";
    private static final String SP_USER_PREFERENCES = "sp_user_preferences";

    private static SharedPrefUtil instance;

    public static SharedPrefUtil getInstance()
    {
        if (instance == null)
            instance = new SharedPrefUtil();

        return instance;
    }

    public void saveCookies(List<Cookie> cookies){
        setPreferenceArray(SP_COOKIES, cookies);
    }

    public List<Cookie> getCookies()
    {
        return (List<Cookie>) getPreferenceArray(SP_COOKIES, new TypeToken<ArrayList<Cookie>>()
        {
        }.getType());
    }
    public void removePreference(String name)
    {
        SharedPreferences.Editor prefEditor = getSharedPreferenceEditor();
        prefEditor.remove(name);
        boolean status = prefEditor.commit();
    }

    public boolean getPreference(String name, boolean defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());

        return pref.getBoolean(name, defaultValue);
    }

    public String getPreference(String name, String defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        return pref.getString(name, defaultValue);
    }

    public Set<String> getPreference(String name, Set<String> defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        return pref.getStringSet(name, defaultValue);
    }

    public float getPreference(String name, float defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        return pref.getFloat(name, defaultValue);
    }

    public int getPreference(String name, int defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        return pref.getInt(name, defaultValue);
    }

    public long getPreference(String name, long defaultValue)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        return pref.getLong(name, defaultValue);
    }

    public Object getPreferenceObject(String name, Class klass)
    {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
        Gson gson = new Gson();
        String json = pref.getString(name, "");

        Object obj = null;
        if (gson != null)
            obj = gson.fromJson(json, klass);

        return obj;
    }

    public ArrayList<T> getPreferenceArray(String name, Type responseListType)
    {
        ArrayList<T> items = new ArrayList<>();
        try
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext());
            Gson gson = new Gson();
            String json = pref.getString(name, "");

            ArrayList<T> parsedItemList = gson.fromJson(json, responseListType);

            if (parsedItemList != null && parsedItemList.size() > 0)
                items.addAll(parsedItemList);
        }
        catch (Exception e)
        {
        }

        return items;
    }

    private SharedPreferences.Editor getSharedPreferenceEditor()
    {
        return PreferenceManager.getDefaultSharedPreferences(AppSystem.Companion.getContext()).edit();
    }

    public void setPreference(String name, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putString(name, value);
        editor.commit();
    }

    public void setPreference(String name, boolean value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putBoolean(name, value);
        editor.commit();
    }
    public void setBooleanPreference(String name, boolean value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putBoolean(name, value);
        editor.commit();
    }
    public void setPreference(String name, Set<String> value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putStringSet(name, value);
        editor.commit();
    }

    public void setPreferenceArray(String name, List<Cookie> array)
    {
        Gson gson = new Gson();

        String listString = gson.toJson(array, new TypeToken<ArrayList<T>>()
        {
        }.getType());

        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putString(name, listString);
        editor.commit();
    }

    public void setPreference(String name, int value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putInt(name, value);
        editor.commit();
    }

    public void setPreference(String name, float value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putFloat(name, value);
        editor.commit();
    }

    public void setPreference(String name, long value)
    {
        SharedPreferences.Editor editor = getSharedPreferenceEditor();
        editor.putLong(name, value);
        editor.commit();
    }

    public void setPreferences(String name, Object value)
    {
        SharedPreferences.Editor prefsEditor = getSharedPreferenceEditor();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public void clearPreferences()
    {
        SharedPreferences.Editor prefsEditor = getSharedPreferenceEditor();
        prefsEditor.clear();
        prefsEditor.commit();
    }


}

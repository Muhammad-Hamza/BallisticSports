package com.example.sportsballistics.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sportsballistics.AppSystem;
import com.example.sportsballistics.data.remote.login.UserResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;

public class SharedPrefUtil<T>
{

    public static final String SP_COOKIES = "SP_COOKIES";
    public static final String USER = "USER";


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
  public void saveUser(UserResponse res){
        setPreferences(USER, res);
    }

    public UserResponse getUser(){
        return (UserResponse) getPreferenceObject(USER,UserResponse.class);
    }
    public List<Cookie> getCookies()
    {
        return (List<Cookie>) getPreferenceArray(SP_COOKIES, new TypeToken<ArrayList<Cookie>>()
        {
        }.getType());
    }
    public boolean isUserLoggedIn(){

        return getCookies().size() > 0;
    }
    public void logout(){
        removePreference(SP_COOKIES);
        removePreference(USER);
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

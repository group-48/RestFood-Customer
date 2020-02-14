package com.dash.restfood_customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setShop(String data) {
        prefs.edit().putString("Shop", data).commit();
    }

    public void setBrowse(String aFalse) {
        prefs.edit().putString("Browse", aFalse).commit();
    }

    public void setMenu(int flag){
        prefs.edit().putInt("Menu",flag).commit();
    }

    public int getMenu(){
        int flag=prefs.getInt("Menu",0);
        return flag;
    }

    public String getShop() {
        String shop = prefs.getString("Shop","");
        return shop;
    }

    public String getBrowse() {
        String browse = prefs.getString("Browse","");
        return browse;
    }
}

package com.ocean.firebasechatappdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;

    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "sharedcheckLogin";
    private static final String User_Id = "userid";
    private static final String IS_LOGIN = "islogin";
    private static final String userPhoneNum = "userPhoneNum";

    //todo: constructor of session manager class
    public SessionManager(Context context) {

        this.context = context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        editor = sharedprefernce.edit();

    }

    //todo: logged in boolean function for false
    public Boolean notLoggedIn() {
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }

    //todo: logged in boolean function for true
    public void loggedIn() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    //Todo: for phone number
    public String getUserPhoneNum(){
        return sharedprefernce.getString(userPhoneNum,"DEFAULT");
    }

    public void setUserPhoneNum(String userPhoneNum){
        editor.putString(userPhoneNum, userPhoneNum);
        editor.commit();
    }
}

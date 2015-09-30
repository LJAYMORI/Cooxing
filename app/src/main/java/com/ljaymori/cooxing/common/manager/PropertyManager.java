package com.ljaymori.cooxing.common.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ljaymori.cooxing.common.CooxingApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PropertyManager {
    // singleton
    private static PropertyManager instance;

    private PropertyManager() {
        mPrefs = CooxingApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }


    // 구현부분
    private static final String PREF_NAME = "cooxingPref";
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;


    /**********
     * GCM
     **********/
    private String regId;
    private static final String REG_ID = "regId";

    public void setRegistrationId(String regId) {
        this.regId = regId;
        mEditor.putString(REG_ID, regId);
        mEditor.commit();
    }

    public String getRegistrationId() {
        if (regId == null) {
            regId = mPrefs.getString(REG_ID, "");
        }
        return regId;
    }

    private Boolean isRegistered;
    private static final String IS_REGISTER = "register";

    public void setRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
        mEditor.putBoolean(IS_REGISTER, isRegistered);
        mEditor.commit();
    }

    public boolean isRegistered() {
        if (isRegistered == null) {
            isRegistered = mPrefs.getBoolean(IS_REGISTER, false);
        }
        return isRegistered;
    }

    /**********
     * GCM 끝
     **********/

    // 가입 경로
    private String joinPath;
    private static final String JOIN_PATH = "joinPath";

    public String getJoinPath() {
        if (joinPath == null) {
            joinPath = mPrefs.getString(JOIN_PATH, "");
        }
        return joinPath;
    }

    public void setJoinPath(String joinPath) {
        this.joinPath = joinPath;
        mEditor.putString(JOIN_PATH, joinPath);
        mEditor.commit();
    }


    // ID
    private String userID;
    private static final String USER_ID = "userID";

    public String getUserID() {
        if (userID == null) {
            userID = mPrefs.getString(USER_ID, "");
        }
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        mEditor.putString(USER_ID, userID);
        mEditor.commit();
    }

    // PASSWORD
    private String userPW;
    private static final String USER_PASSWORD = "userPW";

    public String getUserPW() {
        if (userPW == null) {
            userPW = mPrefs.getString(USER_PASSWORD, "");
        }
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
        mEditor.putString(USER_PASSWORD, userPW);
        mEditor.commit();
    }

    // 검색기록
//    private String searchList;
    private static final String SEARCH_LIST = "search_list";

    public ArrayList<String> getSearchList() {
        ArrayList<String> list;
        Set<String> stringSet = mPrefs.getStringSet(SEARCH_LIST, new HashSet<String>());
        list = new ArrayList<String>(stringSet);

        return list;
    }

    public void setSearchList(ArrayList<String> list) {
        Set<String> stringSet = new HashSet<String>(list);
        mEditor.putStringSet(SEARCH_LIST, stringSet);
        mEditor.commit();
    }

    public void addSearchList(String keyword) {
        ArrayList<String> list = getSearchList();
        list.add(keyword);

        setSearchList(list);
    }

    public int removeSearchList(String keyword) {
        ArrayList<String> list = getSearchList();
        list.remove(keyword);

        setSearchList(list);
        return list.size();
    }

    /***********
     * 기타 구현부분
     **************/
    public void logout() {
        setJoinPath("");
        setUserID("");
        setUserPW("");
    }
}

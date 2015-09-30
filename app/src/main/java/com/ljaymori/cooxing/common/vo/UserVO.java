package com.ljaymori.cooxing.common.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class UserVO implements Serializable {
    private String _id;
    private int interestIcon;

    private String userID;
    private String pw;

    private String nickname;
    private String intro;
    private String profileImage;
    private int point;

    private ArrayList<String> ingredients = new ArrayList<String>();

    private ArrayList<String> following = new ArrayList<String>();
    private ArrayList<String> follower = new ArrayList<String>();

    private ArrayList<String> recipes = new ArrayList<String>();
    private ArrayList<String> bookmarks = new ArrayList<String>();
    private ArrayList<String> history = new ArrayList<String>();

    private String joinPath;
    private String pushKey;
    private String regDate;
    private String accDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getInterestIcon() {
        return interestIcon;
    }

    public void setInterestIcon(int interestIcon) {
        this.interestIcon = interestIcon;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public ArrayList<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<String> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public String getJoinPath() {
        return joinPath;
    }

    public void setJoinPath(String joinPath) {
        this.joinPath = joinPath;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getAccDate() {
        return accDate;
    }

    public void setAccDate(String accDate) {
        this.accDate = accDate;
    }
}

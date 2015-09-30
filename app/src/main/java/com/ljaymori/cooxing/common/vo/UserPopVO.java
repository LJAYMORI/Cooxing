package com.ljaymori.cooxing.common.vo;

import java.util.ArrayList;

public class UserPopVO {
    private String _id;
    private int interestIcon;

    private String userID;
    private String pw;

    private String nickname;
    private String intro;
    private String profileImage;
    private int point;

    private ArrayList<IngredientVO> ingredients = new ArrayList<IngredientVO>();

    private ArrayList<UserVO> following = new ArrayList<UserVO>();
    private ArrayList<UserVO> follower = new ArrayList<UserVO>();

    private ArrayList<RecipeVO> recipes = new ArrayList<RecipeVO>();
    private ArrayList<RecipeVO> bookmarks = new ArrayList<RecipeVO>();
    private ArrayList<RecipeVO> history = new ArrayList<RecipeVO>();


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

    public ArrayList<IngredientVO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientVO> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<UserVO> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<UserVO> following) {
        this.following = following;
    }

    public ArrayList<UserVO> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<UserVO> follower) {
        this.follower = follower;
    }

    public ArrayList<RecipeVO> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<RecipeVO> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<RecipeVO> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<RecipeVO> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<RecipeVO> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<RecipeVO> history) {
        this.history = history;
    }
}

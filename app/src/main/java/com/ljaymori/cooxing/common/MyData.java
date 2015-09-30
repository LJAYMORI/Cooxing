package com.ljaymori.cooxing.common;

import com.ljaymori.cooxing.common.vo.IngredientVO;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.common.vo.UserPopVO;
import com.ljaymori.cooxing.common.vo.UserVO;

import java.util.ArrayList;

public class MyData {
    private static MyData instance;

    private MyData() {
    }

    public static MyData getInstance() {
        if (instance == null) {
            instance = new MyData();
        }
        return instance;
    }

    /** instances */
    private String _id;
    private String userID;
    private String nickname;
    private String intro;
    private String profileImage;
    private String pushKey;
    private int point;
    private ArrayList<String> ingredients = new ArrayList<String>();
    private ArrayList<String> following = new ArrayList<String>();
    private ArrayList<String> follower = new ArrayList<String>();
    private ArrayList<String> bookmarks = new ArrayList<String>();
    private ArrayList<String> recipes = new ArrayList<String>();
    private ArrayList<String> history = new ArrayList<String>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
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

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<String> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }


    /********************************************************************************************/

    public void setMyData(UserPopVO userVO) {
        set_id(userVO.get_id());
        setNickname(userVO.getNickname());
        setIntro(userVO.getIntro());
        setProfileImage(userVO.getProfileImage());
        setRecipes(convertRecipeID(userVO.getRecipes()));
        setBookmarks(convertRecipeID(userVO.getBookmarks()));
        setHistory(convertRecipeID(userVO.getHistory()));
        setFollower(convertUserID(userVO.getFollower()));
        setFollowing(convertUserID(userVO.getFollowing()));
        setIngredients(convertIngredientID(userVO.getIngredients()));
        setPoint(userVO.getPoint());
    }

    public void setMyData(UserVO userVO) {
        set_id(userVO.get_id());
        setNickname(userVO.getNickname());
        setIntro(userVO.getIntro());
        setProfileImage(userVO.getProfileImage());
        setRecipes(userVO.getRecipes());
        setBookmarks(userVO.getBookmarks());
        setHistory(userVO.getHistory());
        setFollower(userVO.getFollower());
        setFollowing(userVO.getFollowing());
        setIngredients(userVO.getIngredients());
        setPoint(userVO.getPoint());
    }

    private ArrayList<String> convertRecipeID(ArrayList<RecipeVO> recipes) {
        ArrayList<String> list = new ArrayList<String>();
        for(RecipeVO vo : recipes) {
            String id = vo.get_id();

            list.add(id);
        }
        return list;
    }

    private ArrayList<String> convertUserID(ArrayList<UserVO> users) {
        ArrayList<String> list = new ArrayList<String>();
        for(UserVO vo : users) {
            String id = vo.get_id();

            list.add(id);
        }
        return list;
    }

    private ArrayList<String> convertIngredientID(ArrayList<IngredientVO> ingredients) {
        ArrayList<String> list = new ArrayList<String>();
        for(IngredientVO vo : ingredients) {
            String id = vo.get_id();

            list.add(id);
        }
        return list;
    }
}

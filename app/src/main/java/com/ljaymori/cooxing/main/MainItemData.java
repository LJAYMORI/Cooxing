package com.ljaymori.cooxing.main;

import java.util.ArrayList;

public class MainItemData {

    private String menu_id;

    private String mainPicturePath;
    private ArrayList<StepItemData> stepList = new ArrayList<StepItemData>();

    private boolean like;

    private String recipeName;

    private String userPicturePath;
    private String nickname;

    private int likeCount;
    private int replyCount;

    private boolean isSwiped;




    /**
     * Getter Setter
     */
    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMainPicturePath() {
        return mainPicturePath;
    }

    public void setMainPicturePath(String mainPicturePath) {
        this.mainPicturePath = mainPicturePath;
    }

    public ArrayList<StepItemData> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<StepItemData> list) {
        this.stepList = list;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getUserPicturePath() {
        return userPicturePath;
    }

    public void setUserPicturePath(String userPicturePath) {
        this.userPicturePath = userPicturePath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isSwiped() {
        return isSwiped;
    }

    public void setIsSwiped(boolean isSwiped) {
        this.isSwiped = isSwiped;
    }
}

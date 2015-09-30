package com.ljaymori.cooxing.profile;

import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.common.vo.UserPopVO;

public class ProfileData {

    private int type;
    private String message;
    private int focusedTabPosition = 0;

    private UserPopVO user;
    private RecipeVO recipe;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFocusedTabPosition() {
        return focusedTabPosition;
    }

    public void setFocusedTabPosition(int focusedTabPosition) {
        this.focusedTabPosition = focusedTabPosition;
    }

    public UserPopVO getUser() {
        return user;
    }

    public void setUser(UserPopVO user) {
        this.user = user;
    }

    public RecipeVO getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeVO recipe) {
        this.recipe = recipe;
    }
}

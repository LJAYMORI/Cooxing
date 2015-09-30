package com.ljaymori.cooxing.common.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeVO implements Serializable {
    private String _id;
    private String title;
    private String description;

    private ArrayList<String> bookmarkers = new ArrayList<String>();
    private ArrayList<CommentVO> comments = new ArrayList<CommentVO>();

    private ArrayList<IngredientVO> ingredients = new ArrayList<IngredientVO>();
    private ArrayList<String> hashtags = new ArrayList<String>();
    private ArrayList<String> images = new ArrayList<String>();
    private ArrayList<String> steps = new ArrayList<String>();

    private UserVO user;

    private String registerDate;


    /**
     * Getter & Setter
     */

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getBookmarkers() {
        return bookmarkers;
    }

    public void setBookmarkers(ArrayList<String> bookmarkers) {
        this.bookmarkers = bookmarkers;
    }

    public ArrayList<CommentVO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentVO> comments) {
        this.comments = comments;
    }

    public ArrayList<IngredientVO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientVO> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

}

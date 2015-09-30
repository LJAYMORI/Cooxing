package com.ljaymori.cooxing.search;

public class UserSearchItemData {
    private String _id;
    private String imageURL;
    private String userName;
    private boolean isFollowing;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}

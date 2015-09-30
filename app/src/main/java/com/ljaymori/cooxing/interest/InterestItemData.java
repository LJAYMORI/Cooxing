package com.ljaymori.cooxing.interest;

public class InterestItemData {

    private int type;
    private boolean checked = false;

    private String _id;
    private String name;
    private int ingredientType;
    private String image;
    private int count;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIngredientType() {
        return ingredientType;
    }

    public void setIngredientType(int ingredientType) {
        this.ingredientType = ingredientType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.ljaymori.cooxing.write.ingredient;

public class IngredientItemData {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll(" ", "_");
    }

}

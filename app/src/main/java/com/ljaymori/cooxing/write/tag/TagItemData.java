package com.ljaymori.cooxing.write.tag;

public class TagItemData {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        char ch = name.charAt(0);
        if(ch != '#') {
            name = "#" + name;
        }

        this.name = name;
    }
}

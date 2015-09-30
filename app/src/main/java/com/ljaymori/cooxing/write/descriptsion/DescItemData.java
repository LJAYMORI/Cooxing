package com.ljaymori.cooxing.write.descriptsion;

public class DescItemData {

    private int type;
    private boolean isActivated;
    private int count;
    private String filePath;
    private String description = new String("");


    public DescItemData() {
        description = new String();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.ljaymori.cooxing.write.image;

import com.ljaymori.cooxing.write.album.MediaData;

public class WriteImageItemData {
    private int countNum = -1;
    private MediaData mediaData;

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public MediaData getMediaData() {
        return mediaData;
    }

    public void setMediaData(MediaData mediaData) {
        this.mediaData = mediaData;
    }
}

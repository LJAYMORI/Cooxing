package com.ljaymori.cooxing.write;

public class WriteFocus {

    private int currentPosition;
    private int focusedEditText;
    private int positionOfRecyclerView;

    public WriteFocus() {
        currentPosition = 0;
        focusedEditText = -1;
        positionOfRecyclerView = -1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getFocusedEditText() {
        return focusedEditText;
    }

    public void setFocusedEditText(int focusedEditText) {
        this.focusedEditText = focusedEditText;
    }

    public int getPositionOfRecyclerView() {
        return positionOfRecyclerView;
    }

    public void setPositionOfRecyclerView(int positionOfRecyclerView) {
        this.positionOfRecyclerView = positionOfRecyclerView;
    }
}

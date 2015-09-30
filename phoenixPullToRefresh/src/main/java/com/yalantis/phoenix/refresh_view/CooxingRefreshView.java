package com.yalantis.phoenix.refresh_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.R;
import com.yalantis.phoenix.util.Utils;

public class CooxingRefreshView extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private final static float BACKGROUND_RATIO = 0.65f;
    private static final float BACKGROUND_INITIAL_SCALE = 1.05f;

    private final static float BOWL_RATIO = 0.22f;
    private static final float BOWL_INITIAL_SCALE = 1.20f;
    private static final float BOWL_FINAL_SCALE = 1.30f;

    private final static float BUBBLE_FINAL_SCALE = 0.75f;
    private static final float BUBBLE_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final float BUBBLE_FINAL_ROTATE_GROWTH = 1.5f;

    private final static float COVER_FINAL_SCALE = 0.75f;
    private static final float COVER_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final float COVER_FINAL_ROTATE_GROWTH = 1.5f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private PullToRefreshView mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private int mBackgroundHeight;
    private float mBackgroundTopOffset;
    private float mBackgroundMoveOffset;

    private int mBowlHeight;
    private float mBowlInitialTopOffset;
    private float mBowlFinalTopOffset;
    private float mBowlMoveOffset;

    private int mBubbleSize = 100;
    private float mBubbleLeftOffset;
    private float mBubbleTopOffset;

    private int mCoverSize = 100;
    private float mCoverLeftOffset;
    private float mCoverTopOffset;

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private Bitmap mBackground;
    private Bitmap mBowl;
    private Bitmap mBubble;
    private Bitmap mCover;

    private boolean isRefreshing = false;


    public CooxingRefreshView(Context context, final PullToRefreshView parent) {
        super(context, parent);

        mParent = parent;
        mMatrix = new Matrix();

        setupAnimations();
        parent.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(parent.getWidth());
            }
        });
    }

    public void initiateDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        mScreenWidth = viewWidth;

        mBackgroundHeight = (int) (BACKGROUND_RATIO * mScreenWidth);
        mBackgroundTopOffset = (mBackgroundHeight * 0.38f);
        mBackgroundMoveOffset = Utils.convertDpToPixel(getContext(), 15);

        mBowlHeight = (int) (BOWL_RATIO * mScreenWidth);
        mBowlInitialTopOffset = (mParent.getTotalDragDistance() - mBowlHeight * BOWL_INITIAL_SCALE);
        mBowlFinalTopOffset = (mParent.getTotalDragDistance() - mBowlHeight * BOWL_FINAL_SCALE);
        mBowlMoveOffset = Utils.convertDpToPixel(getContext(), 10);

        mBubbleLeftOffset = 0.3f * (float) mScreenWidth;
        mBubbleTopOffset = (mParent.getTotalDragDistance() * 0.1f);

        mCoverLeftOffset = 0.5f * (float) mScreenWidth;
        mCoverTopOffset = (mParent.getTotalDragDistance() * 0.4f);

        mTop = -mParent.getTotalDragDistance();

        createBitmaps();
    }

    private void createBitmaps() {
        mBackground = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.background_cooxing);
        mBackground = Bitmap.createScaledBitmap(mBackground, mScreenWidth, mBowlHeight, true);
        mBowl = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bowl_cooxing);
        mBowl = Bitmap.createScaledBitmap(mBowl, mScreenWidth, (int) (mScreenWidth * BOWL_RATIO), true);
        mBubble = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bubble_cooxing);
        mBubble = Bitmap.createScaledBitmap(mBubble, mBubbleSize, mBubbleSize, true);
        mCover = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cover_cooxing);
        mCover = Bitmap.createScaledBitmap(mCover, mCoverSize, mCoverSize, true);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    public void setPercent(float percent) {
        mPercent = percent;
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawBackground(canvas);
        drawBubble(canvas);
        drawBowl(canvas);
//        drawCover(canvas);

        canvas.restoreToCount(saveCount);
    }

    private void drawBackground(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float backgroundScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;

        if (scalePercentDelta > 0) {
            /** Change skyScale between {@link #SKY_INITIAL_SCALE} and 1.0f depending on {@link #mPercent} */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            backgroundScale = BACKGROUND_INITIAL_SCALE - (BACKGROUND_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
            backgroundScale = BACKGROUND_INITIAL_SCALE;
        }

        float offsetX = -(mScreenWidth * backgroundScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() - mBackgroundTopOffset // Offset canvas moving
                - mBackgroundHeight * (backgroundScale - 1.0f) / 2 // Offset sky scaling
                + mBackgroundMoveOffset * dragPercent; // Give it a little move top -> bottom

        matrix.postScale(backgroundScale, backgroundScale);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mBackground, matrix, null);
    }

    private void drawBowl(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float bowlScale;
        float bowlTopOffset;
        float bowlMoveOffset;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;

        if (scalePercentDelta > 0) {
            /**
             * Change townScale between {@link #TOWN_INITIAL_SCALE} and {@link #TOWN_FINAL_SCALE} depending on {@link #mPercent}
             * Change townTopOffset between {@link #mTownInitialTopOffset} and {@link #mTownFinalTopOffset} depending on {@link #mPercent}
             */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            bowlScale = BOWL_INITIAL_SCALE + (BOWL_FINAL_SCALE - BOWL_INITIAL_SCALE) * scalePercent;
            bowlTopOffset = mBowlInitialTopOffset - (mBowlFinalTopOffset - mBowlInitialTopOffset) * scalePercent;
            bowlMoveOffset = mBowlMoveOffset * (1.0f - scalePercent);
        } else {
            float scalePercent = dragPercent / SCALE_START_PERCENT;
            bowlScale = BOWL_INITIAL_SCALE;
            bowlTopOffset = mBowlInitialTopOffset;
            bowlMoveOffset = mBowlMoveOffset * scalePercent;
        }

        float offsetX = -(mScreenWidth * bowlScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() // Offset canvas moving
                + bowlTopOffset
                - mBowlHeight * (bowlScale - 1.0f) / 2 // Offset town scaling
                + bowlMoveOffset; // Give it a little move

        matrix.postScale(bowlScale, bowlScale);
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mBowl, matrix, null);
    }

    private void drawBubble(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = mPercent;
        if (dragPercent > 1.0f) { // Slow down if pulling over set height
            dragPercent = (dragPercent + 9.0f) / 10;
        }

        float bubbleRadius = (float) mBubbleSize / 2.0f;
        float bubbleRotateGrowth = BUBBLE_INITIAL_ROTATE_GROWTH;

        float offsetX = mBubbleLeftOffset;
        float offsetY = mBubbleTopOffset
                + (mParent.getTotalDragDistance() / 2) * (1.0f - dragPercent) // Move the sun up
                - mTop; // Depending on Canvas position

        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            float bubbleScale = 1.0f - (1.0f - BUBBLE_FINAL_SCALE) * scalePercent;
            bubbleRotateGrowth += (BUBBLE_FINAL_ROTATE_GROWTH - BUBBLE_INITIAL_ROTATE_GROWTH) * scalePercent;

            matrix.preTranslate(offsetX + (bubbleRadius - bubbleRadius * bubbleScale), offsetY * (2.0f - bubbleScale));
            matrix.preScale(bubbleScale, bubbleScale);

            offsetX += bubbleRadius;
            offsetY = offsetY * (2.0f - bubbleScale) + bubbleRadius * bubbleScale;
        } else {
            matrix.postTranslate(offsetX, offsetY);

            offsetX += bubbleRadius;
            offsetY += bubbleRadius;
        }


        matrix.postScale(
                (isRefreshing ? 1 : 1),
                (isRefreshing ? 1 : 1)
        );
//        matrix.postRotate(
//                (isRefreshing ? -360 : 360) * mRotate * (isRefreshing ? 1 : sunRotateGrowth),
//                offsetX,
//                offsetY);

        canvas.drawBitmap(mBubble, matrix, null);
    }

    @Override
    public void start() {

    }


    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }



    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidateSelf();
    }

    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }
}

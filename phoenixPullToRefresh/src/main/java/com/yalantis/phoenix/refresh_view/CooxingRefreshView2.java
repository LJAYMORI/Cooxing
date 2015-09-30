package com.yalantis.phoenix.refresh_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.R;
import com.yalantis.phoenix.util.Utils;

public class CooxingRefreshView2 extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private PullToRefreshView mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private final static float BACKGROUND_RATIO = 0.2f;
    private static final float BACKGROUND_INITIAL_SCALE = 1.05f;

    private int mBackgroundHeight;
    private float mBackgroundTopOffset;
    private float mBackgroundMoveOffset;

    private Bitmap mBackground;

    private boolean isRefreshing = false;

    private final int[] REFRESH_IMAGES = new int[]{
            R.drawable.refresh_00, R.drawable.refresh_01, R.drawable.refresh_02, R.drawable.refresh_03,
            R.drawable.refresh_04, R.drawable.refresh_05, R.drawable.refresh_06, R.drawable.refresh_07,
            R.drawable.refresh_08, R.drawable.refresh_09, R.drawable.refresh_10, R.drawable.refresh_11,
            R.drawable.refresh_12, R.drawable.refresh_13, R.drawable.refresh_14, R.drawable.refresh_15,
            R.drawable.refresh_16, R.drawable.refresh_17, R.drawable.refresh_18, R.drawable.refresh_19,
            R.drawable.refresh_20, R.drawable.refresh_21, R.drawable.refresh_22, R.drawable.refresh_23,
            R.drawable.refresh_24
    };


    public CooxingRefreshView2(Context context, final PullToRefreshView parent) {
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

        mTop = -mParent.getTotalDragDistance();

        createBitmaps();
    }

    private void createBitmaps() {
        mBackground = BitmapFactory.decodeResource(getContext().getResources(), REFRESH_IMAGES[0]);
        mBackground = Bitmap.createScaledBitmap(mBackground, mScreenWidth, mBackgroundHeight, true);
    }

    private synchronized void setBackground(int index) {
        if(index > -1 && index < REFRESH_IMAGES.length) {
            mBackground = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(getContext().getResources(), REFRESH_IMAGES[index])
                    , mScreenWidth
                    , mBackgroundHeight
                    , true
            );
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawBackground(canvas);

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

        if(isRefreshing) {
            int index = ((int)(mRotate * 100)) / 7 + 10;
            setBackground(index);

        } else {
            if(mRotate <= 1f) {
                int index = ((int)(mRotate * 100)) / 10;
                setBackground(index);
            }

        }

        matrix.postScale(backgroundScale, backgroundScale);
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mBackground, matrix, null);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    public void setPercent(float percent) {
        mPercent = percent;
    }

    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidateSelf();
    }

    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mBackgroundHeight + top);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }


    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }


    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
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

package com.solarexsoft.cropoutimagedemo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by houruhou on 16/03/2018.
 */

@SuppressLint("AppCompatCustomView")
public class ClippingImageView extends ImageView {
    private final Rect mClipRect = new Rect();

    public ClippingImageView(Context context) {
        super(context);
        initClip();
    }

    public ClippingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initClip();
    }

    public ClippingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClip();
    }

    private void initClip() {
        post(new Runnable() {
            @Override
            public void run() {
                setImageCrop(0.5f);
            }
        });
    }

    private void setImageCrop(float value) {
        final Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        final int width = getWidth();
        final int height = getHeight();
        if (width <= 0 || height<= 0)
            return;
        final int clipWidth = (int) (value*width);
        final int clipHeight = (int) (value*height);
        final int left = clipWidth/2;
        final int top = clipHeight/2;
        final int right = width - left;
        final int bottom = height - top;
        mClipRect.set(left, top, right, bottom);
        invalidate();
    }

    private boolean clip() {
        return !mClipRect.isEmpty() && !clipEqualBounds();
    }

    private boolean clipEqualBounds() {
        final int width = getWidth();
        final int height = getHeight();
        return mClipRect.width() == width && mClipRect.height() == height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (clip()) {
            canvas.clipRect(mClipRect);
        }
        super.onDraw(canvas);
    }

    public void toggle() {
        final float[] values = clipEqualBounds()?new float[]{0f, 0.5f}:new float[]{0.5f, 0f};
        ObjectAnimator.ofFloat(this, "imageCrop", values).start();
    }
}

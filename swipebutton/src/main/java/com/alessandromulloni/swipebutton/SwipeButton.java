package com.alessandromulloni.swipebutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeButton extends FrameLayout {

    public interface OnSwipeListener {
        void onSwipeChanged(View view, float level);
        void onSwipeCancelled(View view);
        void onSwipeConfirmed(View view);
    }

    private View button;
    private View finger;
    private View target;

    private OnSwipeListener listener;

    private float scaleTarget = 3.0f;

    public SwipeButton(Context context) {
        super(context);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.swipe_button, this);

        button = findViewById(R.id.button);
        finger = findViewById(R.id.finger);
        target = findViewById(R.id.target);

        setViewScale(target, scaleTarget);
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                finger.setVisibility(View.VISIBLE);
                target.setVisibility(View.VISIBLE);
                scaleUpFinger(event);
                break;

            case MotionEvent.ACTION_MOVE:
                scaleUpFinger(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                finger.setVisibility(View.INVISIBLE);
                target.setVisibility(View.INVISIBLE);

                if (listener != null) {
                    if (finger.getScaleX() >= scaleTarget) {
                        listener.onSwipeConfirmed(this);
                    } else {
                        listener.onSwipeCancelled(this);
                    }
                }

                break;
        }

        return true;
    }

    private void setViewScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    private void scaleUpFinger(MotionEvent e) {
        float scale = getScale(e);
        setViewScale(finger, scale);

        if (listener != null) {
            listener.onSwipeChanged(this, scale);
        }
    }

    private float getScale(MotionEvent e) {
        float halfSize = getWidth() / 2;

        float distX = e.getX() - halfSize;
        float distY = e.getY() - halfSize;
        float dist = (float)Math.sqrt(distX * distX + distY * distY);

        return dist / halfSize;
    }
}

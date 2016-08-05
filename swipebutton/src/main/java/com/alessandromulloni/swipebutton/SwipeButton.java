package com.alessandromulloni.swipebutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SwipeButton extends FrameLayout {

    public interface OnSwipeListener {
        void onSwipeStarted(View view);
        void onSwipeChanged(View view, float level);
        void onSwipeCancelled(View view);
        void onSwipeConfirmed(View view);
    }

    private ImageView button;
    private View finger;
    private View target;

    private OnSwipeListener listener;

    private int src;
    private int background_button = R.drawable.default_button;
    private int background_target = R.drawable.default_target;
    private int background_finger_cancel = R.drawable.default_finger_cancel;
    private int background_finger_confirm = R.drawable.default_finger_confirm;
    private int animation_enter = R.anim.default_enter;
    private int animation_exit_cancel = R.anim.default_exit_cancel;
    private int animation_exit_confirm = R.anim.default_exit_confirm;

    private float scaleTarget = 5.0f;
    private float lastFingerScale = 0.0f;

    public SwipeButton(Context context) {
        super(context);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        loadAttrs(context, attrs);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        loadAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        loadAttrs(context, attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.swipe_button, this);

        button = (ImageView)findViewById(R.id.button);
        finger = findViewById(R.id.finger);
        target = findViewById(R.id.target);

        setViewScale(target, scaleTarget);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SwipeButton,
                0, 0);

        try {
            src = a.getResourceId(R.styleable.SwipeButton_src, src);
            background_button = a.getResourceId(R.styleable.SwipeButton_background_button, background_button);
            background_target = a.getResourceId(R.styleable.SwipeButton_background_target, background_target);
            background_finger_cancel = a.getResourceId(R.styleable.SwipeButton_background_finger_cancel, background_finger_cancel);
            background_finger_confirm = a.getResourceId(R.styleable.SwipeButton_background_finger_confirm, background_finger_confirm);
            animation_enter = a.getResourceId(R.styleable.SwipeButton_animation_enter, animation_enter);
            animation_exit_cancel = a.getResourceId(R.styleable.SwipeButton_animation_exit_cancel, animation_exit_cancel);
            animation_exit_confirm = a.getResourceId(R.styleable.SwipeButton_animation_exit_confirm, animation_exit_confirm);
        } finally {
            a.recycle();
        }

        button.setBackgroundResource(background_button);
        button.setImageResource(src);

        target.setBackgroundResource(background_target);
        finger.setBackgroundResource(background_finger_cancel);
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                blendInUI();
                scaleUpFinger(event);

                if (listener != null) {
                    listener.onSwipeStarted(this);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                scaleUpFinger(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                blendOutUI();

                if (listener != null) {
                    if (isConfirmed()) {
                        listener.onSwipeConfirmed(this);
                    } else {
                        listener.onSwipeCancelled(this);
                    }
                }

                break;
        }

        return true;
    }

    private boolean isConfirmed() {
        return (lastFingerScale >= scaleTarget);
    }

    private void setViewScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    private void blendInUI() {
        fadeInView(finger);
        fadeInView(target);
    }

    private void fadeInView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), animation_enter);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    private void blendOutUI() {
        fadeOutView(finger);
        fadeOutView(target);
    }

    private void fadeOutView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), (isConfirmed() ? animation_exit_confirm : animation_exit_cancel));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    private void scaleUpFinger(MotionEvent e) {
        boolean wasConfirmed = isConfirmed();

        lastFingerScale = getScale(e);
        setViewScale(finger, lastFingerScale);

        if (!wasConfirmed && isConfirmed()) {
            finger.setBackgroundResource(background_finger_confirm);
        } else if (wasConfirmed && !isConfirmed()) {
            finger.setBackgroundResource(background_finger_cancel);
        }

        if (listener != null) {
            listener.onSwipeChanged(this, lastFingerScale / scaleTarget);
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

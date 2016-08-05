package com.alessandromulloni.swipebutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

    private Drawable src_cancel;
    private Drawable src_confirm;

    private Drawable background_button;
    private Drawable background_target;
    private Drawable background_finger_cancel;
    private Drawable background_finger_confirm;

    private int animation_enter;
    private int animation_exit_cancel;
    private int animation_exit_confirm;

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
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SwipeButton,
                0, 0);

        try {
            src_cancel = loadDrawable(context, a, R.styleable.SwipeButton_src_cancel, 0);
            src_confirm = loadDrawable(context, a, R.styleable.SwipeButton_src_confirm, 0);

            background_button = loadDrawable(context, a, R.styleable.SwipeButton_background_button, R.drawable.default_button);
            background_target = loadDrawable(context, a, R.styleable.SwipeButton_background_target, R.drawable.default_target);
            background_finger_cancel = loadDrawable(context, a, R.styleable.SwipeButton_background_finger_cancel, R.drawable.default_finger_cancel);
            background_finger_confirm = loadDrawable(context, a, R.styleable.SwipeButton_background_finger_confirm, R.drawable.default_finger_confirm);

            animation_enter = loadResource(context, a, R.styleable.SwipeButton_animation_enter, R.anim.default_enter);
            animation_exit_cancel = loadResource(context, a, R.styleable.SwipeButton_animation_exit_cancel, R.anim.default_exit_cancel);
            animation_exit_confirm = loadResource(context, a, R.styleable.SwipeButton_animation_exit_confirm, R.anim.default_exit_confirm);

            scaleTarget = a.getFloat(R.styleable.SwipeButton_scale_target, scaleTarget);
        } finally {
            a.recycle();
        }

        setSourceCancel(src_cancel);
        setBackgroundButton(background_button);
        setBackgroundTarget(background_target);
        setBackgroundFingerCancel(background_finger_cancel);

        setViewScale(target, scaleTarget);
    }

    private Drawable loadDrawable(Context context, TypedArray a, int styleableId, int defaultDrawable) {
        int id = a.getResourceId(styleableId, defaultDrawable);
        return context.getResources().getDrawable(id);
    }

    private int loadResource(Context context, TypedArray a, int styleableId, int defaultAnimation) {
        int id = a.getResourceId(styleableId, defaultAnimation);
        return id;
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
                button.setImageDrawable(src_cancel);

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
            button.setImageDrawable(src_confirm);
            finger.setBackground(background_finger_confirm);
        } else if (wasConfirmed && !isConfirmed()) {
            button.setImageDrawable(src_cancel);
            finger.setBackground(background_finger_cancel);
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

    public void setSourceCancel(Drawable drawable) {
        src_cancel = drawable;
        button.setImageDrawable(src_cancel);
    }

    public void setSourceConfirm(Drawable drawable) {
        src_confirm = drawable;
    }

    public void setBackgroundButton(Drawable drawable) {
        background_button = drawable;
        button.setBackground(background_button);
    }

    public void setBackgroundTarget(Drawable drawable) {
        background_target = drawable;
        target.setBackground(background_target);
    }

    public void setBackgroundFingerCancel(Drawable drawable) {
        background_finger_cancel = drawable;
        finger.setBackground(background_finger_cancel);
    }

    public void setBackgroundFingerConfirm(Drawable drawable) {
        background_finger_confirm = drawable;
    }

    public void setAnimationEnter(int animation) {
        animation_enter = animation;
    }

    public void setAnimationExitCancel(int animation) {
        animation_exit_cancel = animation;
    }

    public void setAnimationExitConfirm(int animation) {
        animation_exit_confirm = animation;
    }
}

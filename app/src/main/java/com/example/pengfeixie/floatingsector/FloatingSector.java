package com.example.pengfeixie.floatingsector;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by pengfeixie on 16/1/10.
 */
public class FloatingSector extends RelativeLayout {

    private MainButton mainButton;

    private RevealLayout revealLayout;

    private View background;

    private boolean isOpen = false;

    private Animation openRotation;

    private Animation closeAnimation;

    private int normalColor = getResources().getColor(R.color.material_blue_500);

    private int darkerColor = getResources().getColor(R.color.color_gray);


    public FloatingSector(Context context) {
        super(context);
    }

    public FloatingSector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingSector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingSector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        View root = LayoutInflater.from(context).inflate(R.layout.view_floating_sector, this);
        mainButton = ((MainButton) root.findViewById(R.id.main));
        revealLayout = ((RevealLayout) root.findViewById(R.id.reveal));
        background = root.findViewById(R.id.back);
        background.setVisibility(GONE);

        initAnimation(context);

        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setVisibility(VISIBLE);
                revealLayout.click((int) view.getX() + view.getWidth() / 2
                        , (int) view.getY() + view.getHeight() / 2
                        , 1000);
                if (isOpen) {
                    view.startAnimation(closeAnimation);
                } else {
                    view.startAnimation(openRotation);
                }

            }
        });
    }

    /**
     * init all the needed animation
     *
     * @param context
     */
    private void initAnimation(Context context) {
        openRotation = AnimationUtils.loadAnimation(context, R.anim.rotation_open);
        openRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isOpen = true;
                animateMainButton(normalColor
                        , darkerColor);
                mainButton.getDrawable().setColorFilter(normalColor, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setFillAfter(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        closeAnimation = AnimationUtils.loadAnimation(context, R.anim.rotation_close);
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isOpen = false;
                animateMainButton(darkerColor, normalColor);
                mainButton.getDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animateMainButton(int from, int to) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mainButton.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        colorAnimation.setDuration(300);
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
    }

    public static void setHardwareLayer(View view, boolean enable) {
        if (Build.VERSION.SDK_INT >= 11) {
            if (enable) {
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }
    }
}

package com.droidcon.uk.physicsui.slides.paramsdemo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.droidcon.uk.physicsui.BaseSlide;
import com.droidcon.uk.physicsui.R;
import com.droidcon.uk.physicsui.widget.SpringPlotter;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class SlideDemoCanonicalParams extends BaseSlide implements SpringListener, SeekBar.OnSeekBarChangeListener {

    private static final float DEFAULT_STIFFNESS = 230.2f;
    private static final float DEFAULT_DAMPING = 22f;
    @Bind(R.id.spring_plotter) SpringPlotter mSpringPlotter;
    @Bind(R.id.ball_container) View mBallContainer;
    @Bind(R.id.ball_top) View mBallTop;
    @Bind(R.id.ball_center) View mBallCenter;
    @Bind(R.id.ball_bottom) View mBallBottom;
    @Bind(R.id.slider_container) View mSliderContainer;
    @Bind(R.id.slider_1) SeekBar mStiffnessSlider;
    @Bind(R.id.slider_2) SeekBar mDampingSlider;
    @Bind(R.id.value_1) TextView mStiffnessValueTextView;
    @Bind(R.id.value_2) TextView mDampingValueTextView;

    @NonNull private final SpringSystem mSpringSystem;
    @NonNull private final Spring mSpringGraphPop;
    @NonNull private final Spring mSpringBallContainerPop;
    @NonNull private final Spring mSpringSliderContainerPop;
    @NonNull private Spring mSpring;

    public SlideDemoCanonicalParams(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        mSpringSystem = SpringSystem.create();
        mSpringGraphPop = mSpringSystem.createSpring();
        mSpringBallContainerPop = mSpringSystem.createSpring();
        mSpringSliderContainerPop = mSpringSystem.createSpring();

        mSpringGraphPop.addListener(this);
        mSpringBallContainerPop.addListener(this);
        mSpringSliderContainerPop.addListener(this);

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        initSliders();

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mSpringGraphPop.setCurrentValue(0);
        mSpringBallContainerPop.setCurrentValue(0);
        mSpringSliderContainerPop.setCurrentValue(0);

        mSpringPlotter.setSpring(mSpring);
        mSpring.setRestDisplacementThreshold(.001);
        mSpring.setRestSpeedThreshold(.001);
        mSpring.setEndValue(1);
    }

    private void initSliders() {
        mStiffnessSlider.setMax(1000);
        mStiffnessSlider.setProgress(stiffnessValueToProgress(DEFAULT_STIFFNESS));
        mStiffnessSlider.setOnSeekBarChangeListener(this);

        mDampingSlider.setMax(1000);
        mDampingSlider.setProgress(dampingValueToProgress(DEFAULT_DAMPING));
        mDampingSlider.setOnSeekBarChangeListener(this);

        updateSliderLabels();
    }

    private void updateSliderLabels() {
        final int stiffness = (int) stiffnessProgressToValue(mStiffnessSlider.getProgress());
        final int damping = (int) dampingProgressToValue(mDampingSlider.getProgress());
        mStiffnessValueTextView.setText(String.valueOf(stiffness));
        mDampingValueTextView.setText(String.valueOf(damping));
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mSpringPlotter.setVisibility(View.VISIBLE);
                mSliderContainer.setVisibility(View.VISIBLE);
                mBallContainer.setVisibility(View.VISIBLE);
                if (animate) {
                    mSpringGraphPop.setEndValue(1);
                    mSpringBallContainerPop.setEndValue(1);
                    mSpringSliderContainerPop.setEndValue(1);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public int getStepCount() {
        return 1;
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mSpringGraphPop) {
            mSpringPlotter.setPivotX(0);
            mSpringPlotter.setPivotY(mSpringPlotter.getHeight());
            mSpringPlotter.setAlpha(value);
            mSpringPlotter.setScaleX(value);
            mSpringPlotter.setScaleY(value);
        } else if (spring == mSpringBallContainerPop) {
            mBallContainer.setAlpha(value);
            mBallContainer.setScaleX(value);
            mBallContainer.setScaleY(value);
        } else if (spring == mSpringSliderContainerPop) {
            mSliderContainer.setAlpha(value);
            mSliderContainer.setScaleX(value);
            mSliderContainer.setScaleY(value);
        } else if (spring == mSpring) {
            mBallTop.setScaleX(value);
            mBallTop.setScaleY(value);
            mBallCenter.setScaleX(value);
            mBallCenter.setScaleY(value);
            mBallBottom.setScaleX(value);
            mBallBottom.setScaleY(value);
        }
    }

    private void setNewSpring(Spring spring) {
        spring.setCurrentValue(0, true /* atRest */);
        spring.setEndValue(1);
        spring.setRestDisplacementThreshold(.000001);
        spring.setRestSpeedThreshold(.000001);
        mSpringPlotter.setSpring(spring);
        mSpring = spring;
        mSpring.addListener(this);
    }

    private int stiffnessValueToProgress(float stiffness) {
        final int min = 100;
        final int max = 1000;
        return (int) (1000 * (stiffness - min) / (max - min));
    }

    private float stiffnessProgressToValue(float progress) {
        final int min = 100;
        final int max = 1000;
        return min + progress * (max - min) / 1000;
    }

    private int dampingValueToProgress(float damping) {
        final int min = 1;
        final int max = 50;
        return (int) (1000 * (damping - min) / (max - min));
    }

    private float dampingProgressToValue(float progress) {
        final int min = 1;
        final int max = 50;
        return min + progress * (max - min) / 1000;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateSliderLabels();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        final float stiffness = stiffnessProgressToValue(mStiffnessSlider.getProgress());
        final float damping = dampingProgressToValue(mDampingSlider.getProgress());

        updateSliderLabels();

        mSpring.destroy();

        final SpringConfig springConfig = new SpringConfig(stiffness, damping);
        final Spring spring = mSpringSystem.createSpring();
        spring.setSpringConfig(springConfig);
        setNewSpring(spring);
    }
}

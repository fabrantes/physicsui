package com.droidcon.uk.physicsui.slides.params;

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
public abstract class SlideDemoParams extends BaseSlide implements SpringListener, SeekBar.OnSeekBarChangeListener {

    private static final float DEFAULT_MASS = 1f;

    @Bind(R.id.spring_plotter) SpringPlotter mSpringPlotter;
    @Bind(R.id.ball_container) View mBallContainer;
    @Bind(R.id.ball_top) View mBallTop;
    @Bind(R.id.ball_center) View mBallCenter;
    @Bind(R.id.ball_bottom) View mBallBottom;
    @Bind(R.id.slider_container) View mSliderContainer;
    @Bind(R.id.slider_1) SeekBar mSlider1;
    @Bind(R.id.slider_2) SeekBar mSlider2;
    @Bind(R.id.value_1) TextView mValue1TextView;
    @Bind(R.id.value_2) TextView mValue2TextView;
    @Bind(R.id.label_1) TextView mLabel1TextView;
    @Bind(R.id.label_2) TextView mLabel2TextView;

    @NonNull private final SpringSystem mSpringSystem;
    @NonNull private final Spring mSpringGraphPop;
    @NonNull private final Spring mSpringBallContainerPop;
    @NonNull private final Spring mSpringSliderContainerPop;
    @NonNull private Spring mSpring;

    public SlideDemoParams(@NonNull Context context, @LayoutRes int layoutId) {
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

        mLabel1TextView.setText(getLabel1StringId());
        mLabel2TextView.setText(getLabel2StringId());
        mSpringPlotter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redrawPlot();
            }
        });

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
        mSlider1.setMax(1000);
        mSlider1.setProgress(valueToProgress1(getDefaultValue1()));
        mSlider1.setOnSeekBarChangeListener(this);

        mSlider2.setMax(1000);
        mSlider2.setProgress(valueToProgress2(getDefaultValue2()));
        mSlider2.setOnSeekBarChangeListener(this);

        updateSliderLabels();
    }

    private void updateSliderLabels() {
        final float value1 = progressToValue1(mSlider1.getProgress());
        final float value2 = progressToValue2(mSlider2.getProgress());
        mValue1TextView.setText(formatFloat(value1));
        mValue2TextView.setText(formatFloat(value2));
    }

    @NonNull
    private String formatFloat(float value) {
        if (value >= 100) {
            return String.format("%.0f", value);
        } else if (value >= 10) {
            return String.format("%.1f", value);
        } else {
            return String.format("%.2f", value);
        }
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateSliderLabels();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        redrawPlot();
    }

    private void redrawPlot() {
        final float value1 = progressToValue1(mSlider1.getProgress());
        final float value2 = progressToValue2(mSlider2.getProgress());
        final float damping = value2ToDamping(value2, DEFAULT_MASS, value1);
        final float stiffness = value1ToStiffness(value1, DEFAULT_MASS, value2);

        updateSliderLabels();

        mSpring.destroy();

        final SpringConfig springConfig = new SpringConfig(stiffness, damping);
        final Spring spring = mSpringSystem.createSpring();
        spring.setSpringConfig(springConfig);
        setNewSpring(spring);
    }

    public abstract int getLabel1StringId();

    public abstract int getLabel2StringId();

    public abstract int valueToProgress1(float value);

    public abstract float progressToValue1(float progress);

    public abstract float value1ToStiffness(float value, float mass, float value2);

    protected abstract float getDefaultValue1();

    public abstract int valueToProgress2(float value);

    public abstract float progressToValue2(float progress);

    public abstract float value2ToDamping(float value, float mass, float value1);

    protected abstract float getDefaultValue2();

}

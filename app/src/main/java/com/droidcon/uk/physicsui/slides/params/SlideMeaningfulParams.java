package com.droidcon.uk.physicsui.slides.params;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidcon.uk.physicsui.BaseSlide;
import com.droidcon.uk.physicsui.R;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class SlideMeaningfulParams extends BaseSlide implements SpringListener {

    @Bind(R.id.param1) TextView mParam1TextView;
    @Bind(R.id.param2) TextView mParam2TextView;

    @NonNull private final Spring mParam1PopSpring;
    @NonNull private final Spring mParam1YSpring;
    @NonNull private final Spring mParam2PopSpring;

    public SlideMeaningfulParams(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mParam1PopSpring = springSystem.createSpring();
        mParam2PopSpring = springSystem.createSpring();
        mParam1YSpring = springSystem.createSpring();

        mParam1PopSpring.addListener(this);
        mParam2PopSpring.addListener(this);
        mParam1YSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mParam1YSpring.setCurrentValue(Double.MIN_VALUE, true);
        mParam1PopSpring.setCurrentValue(0f);
        mParam2PopSpring.setCurrentValue(0);
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mParam1TextView.setVisibility(View.VISIBLE);
                mParam2TextView.setVisibility(View.GONE);
                if (animate) {
                    mParam1PopSpring.setEndValue(1);
                    mParam2PopSpring.setEndValue(0);
                }
                break;
            }
            case 1: {
                mParam1TextView.setVisibility(View.VISIBLE);
                mParam2TextView.setVisibility(View.VISIBLE);
                if (animate) {
                    mParam1PopSpring.setEndValue(1);
                    mParam2PopSpring.setEndValue(1);
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
        return 2;
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float param1Y = mParam1TextView.getY();

        // Override initial positions, if needed.
        if (mParam1YSpring.getCurrentValue() == Double.MIN_VALUE && param1Y != 0) {
            mParam1YSpring.setCurrentValue(param1Y);
        }

        // Dynamic tweening.
        mParam1TextView.setY((float) mParam1YSpring.getCurrentValue());
        mParam1YSpring.setEndValue(param1Y);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mParam1PopSpring) {
            mParam1TextView.setAlpha(value);
            mParam1TextView.setScaleX(value);
            mParam1TextView.setScaleY(value);
        } else if (spring == mParam2PopSpring) {
            mParam2TextView.setAlpha(value);
            mParam2TextView.setScaleX(value);
            mParam2TextView.setScaleY(value);
        } else if (spring == mParam1YSpring) {
            mParam1TextView.setY(value);
        }
    }
}

package com.droidcon.uk.physicsui.slides.impl;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

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
public class SlideDemoSwipeToDelete extends BaseSlide implements SpringListener {

    @Bind(R.id.interactive_demo_container) View mDemoContainer;
    @Bind(R.id.swipeable_item) View mSwipeableItem;

    @NonNull private final Spring mSlidePopSpring;
    @NonNull private final Spring mSwipeTranslationXSpring;

    public SlideDemoSwipeToDelete(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mSlidePopSpring = springSystem.createSpring();
        mSwipeTranslationXSpring = springSystem.createSpring();

        mSlidePopSpring.addListener(this);
        mSwipeTranslationXSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull final View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        mSwipeableItem.setOnTouchListener(
                new HorizontalSwipeTouchFilter()
                        .setListener(new HorizontalSwipeTouchFilter.Listener() {
                            @Override
                            public void onBeganDragging() {
                                mSwipeTranslationXSpring.addListener(SlideDemoSwipeToDelete.this);
                            }

                            @Override
                            public void onFinishDragging() {
                                final double translationX = Math.abs(mSwipeTranslationXSpring.getCurrentValue());
                                if (translationX < mSwipeableItem.getWidth() * .2f) {
                                    mSwipeTranslationXSpring.setEndValue(0);
                                } else if (mSwipeTranslationXSpring.getCurrentValue() > 0) {
                                    // swipe right
                                    // mSwipeTranslationXSpring.setEndValue(view.getWidth());
                                    mSwipeTranslationXSpring.removeAllListeners();
                                    mSwipeableItem.animate()
                                            .translationX(view.getWidth())
                                            .start();
                                } else {
                                    // swipe left
                                    mSwipeTranslationXSpring.setEndValue(-view.getWidth());
                                }
                            }

                            @Override
                            public void onDragTargetUpdate(float x, float y) {
                                mSwipeTranslationXSpring.setEndValue(x);
                            }
                        }));

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mSlidePopSpring.setCurrentValue(0f);
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mSwipeTranslationXSpring.setCurrentValue(0f);
                mSlidePopSpring.setEndValue(1f);
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
        if (spring == mSlidePopSpring) {
            mDemoContainer.setScaleX(value);
            mDemoContainer.setScaleY(value);
            mDemoContainer.setAlpha(value);
        } else if (spring == mSwipeTranslationXSpring) {
            mSwipeableItem.setTranslationX(value);
        }
    }
}

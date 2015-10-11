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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class SlideDemoDragHotCorners extends BaseSlide implements SpringListener {

    @Bind(R.id.interactive_demo_container) View mDemoContainer;
    @Bind(R.id.left_top_target) View mLeftTopTarget;
    @Bind(R.id.right_top_target) View mRightTopTarget;
    @Bind(R.id.left_bottom_target) View mLeftBottomTarget;
    @Bind(R.id.right_bottom_target) View mRightBottomTarget;
    @Bind(R.id.draggable_item) View mDraggableItem;

    @NonNull private final Spring mSlidePopSpring;
    @NonNull private final Spring mTargetsPopSpring;
    @NonNull private final Spring mDraggableTranslationXSpring;
    @NonNull private final Spring mDraggableTranslationYSpring;

    @NonNull List<View> mTargetViews = new ArrayList<>();

    public SlideDemoDragHotCorners(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mSlidePopSpring = springSystem.createSpring();
        mTargetsPopSpring = springSystem.createSpring();
        mDraggableTranslationXSpring = springSystem.createSpring();
        mDraggableTranslationYSpring = springSystem.createSpring();

        mSlidePopSpring.addListener(this);
        mTargetsPopSpring.addListener(this);
        mDraggableTranslationXSpring.addListener(this);
        mDraggableTranslationYSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        mTargetViews.add(mLeftTopTarget);
        mTargetViews.add(mLeftBottomTarget);
        mTargetViews.add(mRightTopTarget);
        mTargetViews.add(mRightBottomTarget);

        final int minMagDistance = getContext().getResources().getDimensionPixelOffset(R.dimen.min_mag_distance);
        mDraggableItem.setOnTouchListener(
                MagneticDragTouchFilter.builder()
                        .setTargetViews(mTargetViews)
                        .setMinMagneticDistance(minMagDistance)
                        .build()
                        .setListener(new MagneticDragTouchFilter.Listener() {
                            @Override
                            public void pnBeganDragging() {
                                mTargetsPopSpring.setEndValue(1f);
                            }

                            @Override
                            public void onFinishDragging() {
                                mTargetsPopSpring.setEndValue(0f);
                            }

                            @Override
                            public void onDragTargetUpdate(float x, float y) {
                                mDraggableTranslationXSpring.setEndValue(x);
                                mDraggableTranslationYSpring.setEndValue(y);
                            }
                        }));

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mSlidePopSpring.setCurrentValue(0f);
        mTargetsPopSpring.setCurrentValue(0f);
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
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
        } else if (spring == mTargetsPopSpring) {
            for (final View view : mTargetViews) {
                view.setScaleX(value);
                view.setScaleY(value);
                view.setAlpha(value);
            }
        } else if (spring == mDraggableTranslationXSpring) {
            mDraggableItem.setTranslationX(value);
        } else if (spring == mDraggableTranslationYSpring) {
            mDraggableItem.setTranslationY(value);
        }
    }
}

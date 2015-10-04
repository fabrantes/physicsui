package com.droidcon.uk.physicsui.slides.impl;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
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
public class SlideInteractiveDrag extends BaseSlide implements SpringListener {

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

    @NonNull
    List<View> mTargetViews = new ArrayList<>();

    public SlideInteractiveDrag(@NonNull Context context, @LayoutRes int layoutId) {
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
        mDraggableItem.setOnTouchListener(new MagneticDragTouchListener(minMagDistance));

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

    private class MagneticDragTouchListener implements View.OnTouchListener {
        private final int[] mLocOnScreen = new int[2];
        private final int mMinMagneticDistance;
        @NonNull final PointF mLastPoint = new PointF();

        MagneticDragTouchListener(int minMagneticDistance) {
            mMinMagneticDistance = minMagneticDistance;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mTargetsPopSpring.setEndValue(1f);
                    mLastPoint.set(event.getRawX(), event.getRawY());
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    mTargetsPopSpring.setEndValue(0f);
                    // dont break let it run the code in 'default'
                }
                default: {
                    final float targetX, targetY;
                    final View targetView = getTargetView(event);
                    if (targetView == mDraggableItem) {
                        // convert full-screen, centered coords (from getRaw*) to local coords
                        final float tLeft = event.getRawX() - mDemoContainer.getLeft() - mDraggableItem.getWidth() / 2;
                        final float tTop = event.getRawY() - mDemoContainer.getTop() - mDraggableItem.getHeight() / 2;
                        targetX = tLeft - mDraggableItem.getLeft();
                        targetY = tTop - mDraggableItem.getTop();
                    } else {
                        targetX = targetView.getLeft() - mDraggableItem.getLeft();
                        targetY = targetView.getTop() - mDraggableItem.getTop();
                    }
                    mDraggableTranslationXSpring.setEndValue(targetX);
                    mDraggableTranslationYSpring.setEndValue(targetY);
                    break;
                }
            }
            return true;
        }

        @NonNull
        private View getTargetView(@NonNull MotionEvent event) {
            int minDistance = Integer.MAX_VALUE;
            View view = mDraggableItem;
            for (final View targetView : mTargetViews) {
                targetView.getLocationOnScreen(mLocOnScreen);
                final float targetCenterX = mLocOnScreen[0] + targetView.getWidth() / 2;
                final float targetCenterY = mLocOnScreen[1] + targetView.getHeight() / 2;
                final float distance = Math.abs(event.getRawX() - targetCenterX) +
                        Math.abs(event.getRawY() - targetCenterY);
                if (distance < minDistance && distance < mMinMagneticDistance) {
                    minDistance = (int) distance;
                    view = targetView;
                }
            }
            return view;
        }
    }
}

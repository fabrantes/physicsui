package com.droidcon.uk.physicsui.slides.title;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
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
public class SlideTitle extends BaseSlide implements SpringListener {

    @Bind(R.id.title_part_left) TextView mTitleLeft;
    @Bind(R.id.title_part_right) TextView mTitleRight;
    @Bind(R.id.author_left) TextView mAuthorLeft;
    @Bind(R.id.author_right) TextView mAuthorRight;
    @Bind(R.id.title_space) Space mTitleSpace;

    @NonNull private final Spring mTitleLeftXSpring;
    @NonNull private final Spring mTitleRightXSpring;
    @NonNull private final Spring mTitleRightScaleSpring;
    @NonNull private final Spring mTitleLeftAlphaSpring;
    @NonNull private final Spring mAuthorPopSpring;

    private final int mPaddingHackSize;

    public SlideTitle(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mTitleLeftXSpring = springSystem.createSpring();
        mTitleRightXSpring = springSystem.createSpring();
        mTitleRightScaleSpring = springSystem.createSpring();
        mTitleLeftAlphaSpring = springSystem.createSpring();
        mAuthorPopSpring = springSystem.createSpring();

        mTitleLeftXSpring.addListener(this);
        mTitleRightXSpring.addListener(this);
        mTitleRightScaleSpring.addListener(this);
        mTitleLeftAlphaSpring.addListener(this);
        mAuthorPopSpring.addListener(this);

        mPaddingHackSize = context.getResources().getDimensionPixelSize(R.dimen.title_slide_padding_hack_size);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        // Set uninitialized state
        mTitleLeftXSpring.setCurrentValue(Double.MIN_VALUE, true);
        mTitleRightXSpring.setCurrentValue(Double.MIN_VALUE, true);
    }

    @Override
    public boolean onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mTitleLeft.setVisibility(View.GONE);
                mTitleSpace.setVisibility(View.GONE);
                mAuthorLeft.setVisibility(View.INVISIBLE);
                mAuthorRight.setVisibility(View.INVISIBLE);
                if (animate) {
                    applyHidingPaddingHack(false);
                    mTitleRightScaleSpring.setCurrentValue(0);
                    mTitleRightScaleSpring.setEndValue(1);
                }
                break;
            }
            case 1: {
                mTitleLeft.setVisibility(View.VISIBLE);
                mTitleSpace.setVisibility(View.VISIBLE);
                if (animate) {
                    applyHidingPaddingHack(true);
                    mTitleLeftAlphaSpring.setCurrentValue(0);
                    mTitleLeftAlphaSpring.setEndValue(1f);
                }
                break;
            }
            case 2: {
                mAuthorLeft.setVisibility(View.VISIBLE);
                mAuthorRight.setVisibility(View.VISIBLE);
                if (animate) {
                    mAuthorPopSpring.setCurrentValue(0);
                    mAuthorPopSpring.setEndValue(1f);
                }
                break;
            }
            default: {
                break;
            }
        }
        return true;
    }

    private void applyHidingPaddingHack(boolean addPading) {
        // XXX padding hack to make sure 'UI' covers 'Physics' while it translates in
        mTitleLeft.setPadding(addPading ? mPaddingHackSize : 0, 0, 0, 0);
        mTitleRight.setPadding(0, 0, addPading ? mPaddingHackSize : 0, 0);
    }

    @Override
    public int getStepCount() {
        return 3;
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float titleLeftX = mTitleLeft.getX();
        final float titleRightX = mTitleRight.getX();

        // Override initial positions, if needed.
        if (mTitleLeftXSpring.getCurrentValue() == Double.MIN_VALUE && titleLeftX != 0) {
            mTitleLeftXSpring.setCurrentValue(titleLeftX + mTitleLeft.getWidth() - mPaddingHackSize);
        }
        if (mTitleRightXSpring.getCurrentValue() == Double.MIN_VALUE && titleRightX != 0) {
            mTitleRightXSpring.setCurrentValue(titleRightX);
        }

        // Dynamic tweening.
        mTitleLeft.setX((float) mTitleLeftXSpring.getCurrentValue());
        mTitleRight.setX((float) mTitleRightXSpring.getCurrentValue());
        mTitleLeftXSpring.setEndValue(titleLeftX);
        mTitleRightXSpring.setEndValue(titleRightX);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mTitleLeftXSpring) {
            mTitleLeft.setX(value);
        } else if (spring == mTitleRightXSpring) {
            mTitleRight.setX(value);
        } else if (spring == mTitleRightScaleSpring) {
            mTitleRight.setScaleX(value);
            mTitleRight.setScaleY(value);
        } else if (spring == mTitleLeftAlphaSpring) {
            mTitleLeft.setAlpha(value);
        } else if (spring == mAuthorPopSpring) {
            mAuthorLeft.setAlpha(value);
            mAuthorLeft.setScaleX(value);
            mAuthorLeft.setScaleY(value);
            mAuthorRight.setAlpha(value);
            mAuthorRight.setScaleX(value);
            mAuthorRight.setScaleY(value);
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) { }

    @Override
    public void onSpringActivate(Spring spring) { }

    @Override
    public void onSpringEndStateChange(Spring spring) { }
}

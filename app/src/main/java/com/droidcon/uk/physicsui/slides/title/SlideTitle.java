package com.droidcon.uk.physicsui.slides.title;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

    @NonNull private final Spring mTitleLeftTranslationXSpring;
    @NonNull private final Spring mTitleRightTranslationXSpring;
    @NonNull private final Spring mTitleRightScaleSpring;
    @NonNull private final Spring mTitleLeftAlphaSpring;
    @NonNull private final Spring mAuthorPopSpring;

    private final int mPaddingHackSize;

    public SlideTitle(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
        final SpringSystem springSystem = SpringSystem.create();
        mTitleLeftTranslationXSpring = springSystem.createSpring();
        mTitleRightTranslationXSpring = springSystem.createSpring();
        mTitleRightScaleSpring = springSystem.createSpring();
        mTitleLeftAlphaSpring = springSystem.createSpring();
        mAuthorPopSpring = springSystem.createSpring();

        mTitleLeftTranslationXSpring.addListener(this);
        mTitleRightTranslationXSpring.addListener(this);
        mTitleRightScaleSpring.addListener(this);
        mTitleLeftAlphaSpring.addListener(this);
        mAuthorPopSpring.addListener(this);

        mPaddingHackSize = context.getResources().getDimensionPixelSize(R.dimen.title_slide_padding_hack_size);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);
    }

    @Override
    public boolean stepTo(int stepIdx, boolean animate) {
        final boolean outOfBounds = stepIdx < 0 || stepIdx >= getStepCount();
        if (stepIdx == getStepIdx() || outOfBounds) {
            return !outOfBounds;
        }

        // TODO Does it make sense to move the animation logic to a SlideTitleAnimationController/Helper class?
        switch (stepIdx) {
            case 0: {
                mTitleLeft.setVisibility(View.GONE);
                mTitleSpace.setVisibility(View.GONE);
                mAuthorLeft.setVisibility(View.INVISIBLE);
                mAuthorRight.setVisibility(View.INVISIBLE);
                if (animate) {
                    mTitleRightScaleSpring.setCurrentValue(0);
                    mTitleRightScaleSpring.setEndValue(1);
                }
                break;
            }
            case 1: {
                mTitleLeft.setVisibility(View.VISIBLE);
                mTitleSpace.setVisibility(View.VISIBLE);
                if (animate) {
                    // XXX padding hack to make sure 'UI' covers 'Physics' while it translates in
                    mTitleLeft.setPadding(mPaddingHackSize, 0, 0, 0);
                    mTitleRight.setPadding(0, 0, mPaddingHackSize, 0);

                    final float oldTitleRightX = mTitleRight.getX();
                    mTitleLeft.getViewTreeObserver().addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    mTitleLeft.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                    final float rightTranslationX = oldTitleRightX - mTitleRight.getX();
                                    final float leftTranslationX = mTitleLeft.getWidth() + mTitleSpace.getWidth() +
                                            rightTranslationX;

                                    mTitleLeftTranslationXSpring.setCurrentValue(leftTranslationX - mPaddingHackSize);
                                    mTitleLeftTranslationXSpring.setEndValue(0);
                                    mTitleRightTranslationXSpring.setCurrentValue(rightTranslationX);
                                    mTitleRightTranslationXSpring.setEndValue(0);
                                    mTitleLeftAlphaSpring.setCurrentValue(0);
                                    mTitleLeftAlphaSpring.setEndValue(1f);
                                }
                            });
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

    @Override
    public int getStepCount() {
        return 3;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        final float value = (float) spring.getCurrentValue();
        if (spring == mTitleLeftTranslationXSpring) {
            mTitleLeft.setTranslationX(value);
        } else if (spring == mTitleRightTranslationXSpring) {
            mTitleRight.setTranslationX(value);
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

package com.droidcon.uk.physicsui.slides.title;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class SlideTheory2 extends BaseSlide implements SpringListener {

    @Bind(R.id.quotes_holder) View mQuotesHolder;
    @Bind(R.id.quote_hookes_law) TextView mHookesTextView;
    @Bind(R.id.quote_damping) TextView mDampingTextview;
    @Bind(R.id.equation_holder) View mEquationHolder;
    @Bind(R.id.left_part_of_equation) View mLeftEqView;
    @Bind(R.id.right_part_first_member) View mFirstMemberView;
    @Bind(R.id.right_part_second_member) View mSecondMemberView;
    @Bind(R.id.spring_mass_damp_system_img) ImageView mSMDSystemImageView;

    @NonNull private final Spring mQuotesHolderYSpring;
    @NonNull private final Spring mEqHolderXSpring;
    @NonNull private final Spring mEqHolderYSpring;
    @NonNull private final Spring mHookesQuotePopSpring;
    @NonNull private final Spring mDampingQuotePopSpring;
    @NonNull private final Spring mEqLeftPopSpring;
    @NonNull private final Spring mEqRightFirstPopSpring;
    @NonNull private final Spring mEqRightSecondPopSpring;
    @NonNull private final Spring mSMDSystemPopSpring;

    public SlideTheory2(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mQuotesHolderYSpring = springSystem.createSpring();
        mEqHolderXSpring = springSystem.createSpring();
        mEqHolderYSpring = springSystem.createSpring();
        mHookesQuotePopSpring = springSystem.createSpring();
        mDampingQuotePopSpring = springSystem.createSpring();
        mEqLeftPopSpring = springSystem.createSpring();
        mEqRightFirstPopSpring = springSystem.createSpring();
        mEqRightSecondPopSpring = springSystem.createSpring();
        mSMDSystemPopSpring = springSystem.createSpring();

        mQuotesHolderYSpring.addListener(this);
        mEqHolderXSpring.addListener(this);
        mEqHolderYSpring.addListener(this);
        mHookesQuotePopSpring.addListener(this);
        mDampingQuotePopSpring.addListener(this);
        mEqLeftPopSpring.addListener(this);
        mEqRightFirstPopSpring.addListener(this);
        mEqRightSecondPopSpring.addListener(this);
        mSMDSystemPopSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        mSMDSystemImageView.setColorFilter(getResources().getColor(R.color.light_yellow), PorterDuff.Mode.MULTIPLY);

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mQuotesHolderYSpring.setCurrentValue(Double.MIN_VALUE);
        mEqHolderXSpring.setCurrentValue(Double.MIN_VALUE);
        mEqHolderYSpring.setCurrentValue(Double.MIN_VALUE);
        mHookesQuotePopSpring.setCurrentValue(0);
        mDampingQuotePopSpring.setCurrentValue(0);
        mEqLeftPopSpring.setCurrentValue(0);
        mEqRightFirstPopSpring.setCurrentValue(0);
        mEqRightSecondPopSpring.setCurrentValue(0);
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mHookesTextView.setVisibility(View.VISIBLE);
                mDampingTextview.setVisibility(View.INVISIBLE);
                mEquationHolder.setVisibility(View.GONE);
                mLeftEqView.setVisibility(View.GONE);
                mFirstMemberView.setVisibility(View.GONE);
                mSecondMemberView.setVisibility(View.GONE);
                mSMDSystemImageView.setVisibility(View.GONE);
                if (animate) {
                    mHookesQuotePopSpring.setEndValue(1);
                    mDampingQuotePopSpring.setEndValue(0);
                    mEqLeftPopSpring.setEndValue(0);
                    mEqRightFirstPopSpring.setEndValue(0);
                    mEqRightSecondPopSpring.setEndValue(0);
                    mSMDSystemPopSpring.setEndValue(0);
                }
                break;
            }
            case 1: {
                mHookesTextView.setVisibility(View.INVISIBLE);
                mDampingTextview.setVisibility(View.VISIBLE);
                mEquationHolder.setVisibility(View.GONE);
                mLeftEqView.setVisibility(View.GONE);
                mFirstMemberView.setVisibility(View.GONE);
                mSecondMemberView.setVisibility(View.GONE);
                mSMDSystemImageView.setVisibility(View.GONE);
                if (animate) {
                    mHookesQuotePopSpring.setEndValue(0);
                    mDampingQuotePopSpring.setEndValue(1);
                    mEqLeftPopSpring.setEndValue(0);
                    mEqRightFirstPopSpring.setEndValue(0);
                    mEqRightSecondPopSpring.setEndValue(0);
                    mSMDSystemPopSpring.setEndValue(0);
                }
                break;
            }
            case 2: {
                mHookesTextView.setVisibility(View.VISIBLE);
                mDampingTextview.setVisibility(View.INVISIBLE);
                mEquationHolder.setVisibility(View.VISIBLE);
                mLeftEqView.setVisibility(View.VISIBLE);
                mFirstMemberView.setVisibility(View.VISIBLE);
                mSecondMemberView.setVisibility(View.GONE);
                mSMDSystemImageView.setVisibility(View.GONE);
                if (animate) {
                    mHookesQuotePopSpring.setEndValue(1);
                    mDampingQuotePopSpring.setEndValue(0);
                    mEqLeftPopSpring.setEndValue(1);
                    mEqRightFirstPopSpring.setEndValue(1);
                    mEqRightSecondPopSpring.setEndValue(0);
                    mSMDSystemPopSpring.setEndValue(0);
                }
                break;
            }
            case 3: {
                mHookesTextView.setVisibility(View.INVISIBLE);
                mDampingTextview.setVisibility(View.VISIBLE);
                mEquationHolder.setVisibility(View.VISIBLE);
                mLeftEqView.setVisibility(View.VISIBLE);
                mFirstMemberView.setVisibility(View.VISIBLE);
                mSecondMemberView.setVisibility(View.VISIBLE);
                mSMDSystemImageView.setVisibility(View.GONE);
                if (animate) {
                    mHookesQuotePopSpring.setEndValue(0);
                    mDampingQuotePopSpring.setEndValue(1);
                    mEqLeftPopSpring.setEndValue(1);
                    mEqRightFirstPopSpring.setEndValue(1);
                    mEqRightSecondPopSpring.setEndValue(1);
                    mSMDSystemPopSpring.setEndValue(0);
                }
                break;
            }
            case 4: {
                mHookesTextView.setVisibility(View.GONE);
                mDampingTextview.setVisibility(View.GONE);
                mEquationHolder.setVisibility(View.VISIBLE);
                mLeftEqView.setVisibility(View.VISIBLE);
                mFirstMemberView.setVisibility(View.VISIBLE);
                mSecondMemberView.setVisibility(View.VISIBLE);
                mSMDSystemImageView.setVisibility(View.VISIBLE);
                if (animate) {
                    mHookesQuotePopSpring.setEndValue(0);
                    mDampingQuotePopSpring.setEndValue(0);
                    mEqLeftPopSpring.setEndValue(1);
                    mEqRightFirstPopSpring.setEndValue(1);
                    mEqRightSecondPopSpring.setEndValue(1);
                    mSMDSystemPopSpring.setEndValue(1);
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
        return 5;
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float quotesY = mQuotesHolder.getY();
        final float eqX = mEquationHolder.getX();
        final float eqY = mEquationHolder.getY();
        if (mQuotesHolderYSpring.getCurrentValue() == Double.MIN_VALUE && quotesY != 0) {
            mQuotesHolderYSpring.setCurrentValue(quotesY);
        }
        if (mEqHolderXSpring.getCurrentValue() == Double.MIN_VALUE && eqX != 0) {
            mEqHolderXSpring.setCurrentValue(eqX);
        }
        if (mEqHolderYSpring.getCurrentValue() == Double.MIN_VALUE && eqY != 0) {
            mEqHolderYSpring.setCurrentValue(eqY);
        }

        mQuotesHolder.setY((float) mQuotesHolderYSpring.getCurrentValue());
        mEquationHolder.setX((float) mEqHolderXSpring.getCurrentValue());
        mEquationHolder.setY((float) mEqHolderYSpring.getCurrentValue());

        mQuotesHolderYSpring.setEndValue(quotesY);
        mEqHolderXSpring.setEndValue(eqX);
        mEqHolderYSpring.setEndValue(eqY);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mQuotesHolderYSpring) {
            mQuotesHolder.setY(value);
        } else if (spring == mEqHolderXSpring) {
            mEquationHolder.setX(value);
        } else if (spring == mEqHolderYSpring) {
            mEquationHolder.setY(value);
        } else if (spring == mDampingQuotePopSpring) {
            mDampingTextview.setAlpha(value);
            mDampingTextview.setScaleX(value);
            mDampingTextview.setScaleY(value);
        } else if (spring == mHookesQuotePopSpring) {
            mHookesTextView.setAlpha(value);
            mHookesTextView.setScaleX(value);
            mHookesTextView.setScaleY(value);
        } else if (spring == mEqLeftPopSpring) {
            mLeftEqView.setAlpha(value);
            mLeftEqView.setScaleX(value);
            mLeftEqView.setScaleY(value);
        } else if (spring == mEqRightFirstPopSpring) {
            mFirstMemberView.setAlpha(value);
            mFirstMemberView.setScaleX(value);
            mFirstMemberView.setScaleY(value);
        } else if (spring == mEqRightSecondPopSpring) {
            mSecondMemberView.setAlpha(value);
            mSecondMemberView.setScaleX(value);
            mSecondMemberView.setScaleY(value);
        } else if (spring == mSMDSystemPopSpring) {
            mSMDSystemImageView.setAlpha(value);
            mSMDSystemImageView.setScaleX(value);
            mSMDSystemImageView.setScaleY(value);
        }
    }
}

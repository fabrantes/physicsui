package com.droidcon.uk.physicsui.slides.params;

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
public class SlideMeaningfulParamsTheory extends BaseSlide implements SpringListener {

    @Bind(R.id.equation_holder) View mEquationHolderView;
    @Bind(R.id.diff_eq_solution) View mDiffEqSolutionView;
    @Bind(R.id.conclusions_container) View mConclusionsContainer;

    @NonNull private final Spring mEqHolderPopSpring;
    @NonNull private final Spring mEqHolderYSpring;
    @NonNull private final Spring mDiffEqPopSpring;
    @NonNull private final Spring mDiffEqYSpring;
    @NonNull private final Spring mConclusionsPopSpring;
    @NonNull private final Spring mConclusionsYSpring;

    public SlideMeaningfulParamsTheory(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mEqHolderPopSpring = springSystem.createSpring();
        mEqHolderYSpring = springSystem.createSpring();
        mDiffEqYSpring = springSystem.createSpring();
        mDiffEqPopSpring = springSystem.createSpring();
        mConclusionsPopSpring = springSystem.createSpring();
        mConclusionsYSpring = springSystem.createSpring();

        mEqHolderPopSpring.addListener(this);
        mEqHolderYSpring.addListener(this);
        mDiffEqPopSpring.addListener(this);
        mDiffEqYSpring.addListener(this);
        mConclusionsPopSpring.addListener(this);
        mConclusionsYSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        // Initialize springs, fade ins are set to 0 whereas positions are set to some unknown value (since we don't
        // know the initial positions for the Views before they are laid out).
        mEqHolderYSpring.setCurrentValue(Double.MIN_VALUE, true);
        mEqHolderPopSpring.setCurrentValue(0f);
        mDiffEqYSpring.setCurrentValue(Double.MIN_VALUE, true);
        mDiffEqPopSpring.setCurrentValue(0f);
        mConclusionsYSpring.setCurrentValue(Double.MIN_VALUE, true);
        mConclusionsPopSpring.setCurrentValue(0f);
    }

    @Override
    public void onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mEquationHolderView.setVisibility(View.VISIBLE);
                mDiffEqSolutionView.setVisibility(View.GONE);
                mConclusionsContainer.setVisibility(View.GONE);
                if (animate) {
                    mEqHolderPopSpring.setEndValue(1);
                    mDiffEqPopSpring.setEndValue(0);
                    mConclusionsPopSpring.setEndValue(0);
                }
                break;
            }
            case 1: {
                mEquationHolderView.setVisibility(View.VISIBLE);
                mDiffEqSolutionView.setVisibility(View.VISIBLE);
                mConclusionsContainer.setVisibility(View.GONE);
                if (animate) {
                    mEqHolderPopSpring.setEndValue(1);
                    mDiffEqPopSpring.setEndValue(1);
                    mConclusionsPopSpring.setEndValue(0);
                }
                break;
            }
            case 2: {
                mEquationHolderView.setVisibility(View.GONE);
                mDiffEqSolutionView.setVisibility(View.VISIBLE);
                mConclusionsContainer.setVisibility(View.VISIBLE);
                if (animate) {
                    mEqHolderPopSpring.setEndValue(0);
                    mDiffEqPopSpring.setEndValue(1);
                    mConclusionsPopSpring.setEndValue(1);
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
        return 3;
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float eqHolderY = mEquationHolderView.getY();
        final float diffEqY = mDiffEqSolutionView.getY();
        final float concY = mConclusionsContainer.getY();

        // Override initial positions, if needed.
        if (mEqHolderYSpring.getCurrentValue() == Double.MIN_VALUE && eqHolderY != 0) {
            mEqHolderYSpring.setCurrentValue(eqHolderY);
        }
        if (mDiffEqYSpring.getCurrentValue() == Double.MIN_VALUE && diffEqY != 0) {
            mDiffEqYSpring.setCurrentValue(diffEqY);
        }
        if (mConclusionsYSpring.getCurrentValue() == Double.MIN_VALUE && concY != 0) {
            mConclusionsYSpring.setCurrentValue(concY);
        }

        // Dynamic tweening.
        mEquationHolderView.setY((float) mEqHolderYSpring.getCurrentValue());
        mEqHolderYSpring.setEndValue(eqHolderY);
        mDiffEqSolutionView.setY((float) mDiffEqYSpring.getCurrentValue());
        mDiffEqYSpring.setEndValue(diffEqY);
        mConclusionsContainer.setY((float) mConclusionsYSpring.getCurrentValue());
        mConclusionsYSpring.setEndValue(concY);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mEqHolderPopSpring) {
            mEquationHolderView.setAlpha(value);
            mEquationHolderView.setScaleX(value);
            mEquationHolderView.setScaleY(value);
        } else if (spring == mDiffEqPopSpring) {
            mDiffEqSolutionView.setAlpha(value);
            mDiffEqSolutionView.setScaleX(value);
            mDiffEqSolutionView.setScaleY(value);
        } else if (spring == mConclusionsPopSpring) {
            mConclusionsContainer.setAlpha(value);
            mConclusionsContainer.setScaleX(value);
            mConclusionsContainer.setScaleY(value);
        } else if (spring == mEqHolderYSpring) {
            mEquationHolderView.setY(value);
        } else if (spring == mDiffEqYSpring) {
            mDiffEqSolutionView.setY(value);
        } else if (spring == mConclusionsYSpring) {
            mConclusionsContainer.setY(value);
        }
    }
}

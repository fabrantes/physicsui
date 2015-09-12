package com.droidcon.uk.physicsui.slides.title;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Html;
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
public class SlideWhySprings extends BaseSlide implements SpringListener {

    @Bind(R.id.springs) TextView mMajorTextView;
    @Bind(R.id.reason1) TextView mReason1TextView;
    @Bind(R.id.reason2) TextView mReason2TextView;

    @NonNull private final Spring mMajorAlphaSpring;
    @NonNull private final Spring mMajorYSpring;
    @NonNull private final Spring mReason1AlphaSpring;
    @NonNull private final Spring mReason1YSpring;
    @NonNull private final Spring mReason2AlphaSpring;

    public SlideWhySprings(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */
        final SpringSystem springSystem = SpringSystem.create();
        mMajorAlphaSpring = springSystem.createSpring();
        mReason1AlphaSpring = springSystem.createSpring();
        mReason2AlphaSpring = springSystem.createSpring();
        mMajorYSpring = springSystem.createSpring();
        mReason1YSpring = springSystem.createSpring();

        mMajorAlphaSpring.addListener(this);
        mReason1AlphaSpring.addListener(this);
        mReason2AlphaSpring.addListener(this);
        mMajorYSpring.addListener(this);
        mReason1YSpring.addListener(this);
    }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);
        mReason2TextView.setText(Html.fromHtml(getResources().getString(R.string.they_feel_nice)));

        // Set uninitialized state
        mMajorYSpring.setCurrentValue(Double.MIN_VALUE, true);
        mReason1YSpring.setCurrentValue(Double.MIN_VALUE, true);
    }

    @Override
    public boolean onStepTo(int stepIdx, boolean animate) {
        switch (stepIdx) {
            case 0: {
                mMajorTextView.setVisibility(View.VISIBLE);
                mReason1TextView.setVisibility(View.GONE);
                mReason2TextView.setVisibility(View.GONE);
                if (animate) {
                    mMajorAlphaSpring.setCurrentValue(0);
                    mMajorAlphaSpring.setEndValue(1);
                }
                break;
            }
            case 1: {
                mMajorTextView.setVisibility(View.VISIBLE);
                mReason1TextView.setVisibility(View.VISIBLE);
                mReason2TextView.setVisibility(View.GONE);
                if (animate) {
                    mReason1AlphaSpring.setCurrentValue(0f);
                    mReason1AlphaSpring.setEndValue(1f);
                }
                break;
            }
            case 2: {
                mMajorTextView.setVisibility(View.VISIBLE);
                mReason1TextView.setVisibility(View.VISIBLE);
                mReason2TextView.setVisibility(View.VISIBLE);
                if (animate) {
                    mReason2AlphaSpring.setCurrentValue(0);
                    mReason2AlphaSpring.setEndValue(1f);
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
    public void onGlobalLayout() {
        super.onGlobalLayout();

        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float majorY = mMajorTextView.getY();
        final float reason1Y = mReason1TextView.getY();

        // Override initial positions, if needed.
        if (mMajorYSpring.getCurrentValue() == Double.MIN_VALUE && majorY != 0) {
            mMajorYSpring.setCurrentValue(majorY);
        }
        if (mReason1YSpring.getCurrentValue() == Double.MIN_VALUE && reason1Y != 0) {
            mReason1YSpring.setCurrentValue(reason1Y);
        }

        // Dynamic tweening.
        mMajorTextView.setY((float) mMajorYSpring.getCurrentValue());
        mReason1TextView.setY((float) mReason1YSpring.getCurrentValue());
        mMajorYSpring.setEndValue(majorY);
        mReason1YSpring.setEndValue(reason1Y);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        /**
         * ALL OF THE CODE HERE WOULD BE POSSIBLE TO GENERATE WITH AN ANNOTATION PROCESSOR
         */

        final float value = (float) spring.getCurrentValue();
        if (spring == mMajorAlphaSpring) {
            mMajorTextView.setAlpha(value);
        } else if (spring == mReason1AlphaSpring) {
            mReason1TextView.setAlpha(value);
        } else if (spring == mReason2AlphaSpring) {
            mReason2TextView.setAlpha(value);
        } else if (spring == mMajorYSpring) {
            mMajorTextView.setY(value);
        } else if (spring == mReason1YSpring) {
            mReason1TextView.setY(value);
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) { }

    @Override
    public void onSpringActivate(Spring spring) { }

    @Override
    public void onSpringEndStateChange(Spring spring) { }

}

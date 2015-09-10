package com.droidcon.uk.physicsui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fabrantes on 06/09/2015.
 */
public abstract class BaseSlide implements Slide {

    @LayoutRes private final int mLayoutId;
    @NonNull private final Context mContext;
    @Nullable private View mView;
    @Nullable private ViewGroup mParentView;
    private int mStepIdx = -1;

    public BaseSlide(@NonNull Context context, @LayoutRes int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        stepTo(mStepIdx);
    }

    @Override
    public int getStepIdx() {
        return mStepIdx;
    }

    public void setStepIdx(int stepIdx) {
        mStepIdx = stepIdx;
    }

    @Override
    public boolean prevStep() {
        return stepTo(getStepIdx() - 1);
    }

    @Override
    public boolean nextStep() {
        return stepTo(getStepIdx() + 1);
    }

    @Override
    public boolean stepTo(int stepIdx) {
        if (stepTo(stepIdx, true)) {
            setStepIdx(stepIdx);
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public View enter(@NonNull ViewGroup viewGroup) {
        if (mView == null) {
            final View view = LayoutInflater.from(mContext).inflate(mLayoutId, viewGroup, true);
            mView = view.findViewById(R.id.slide);
            mParentView = viewGroup;
            onSlideInflated(mView, mParentView);
        }
        stepTo(0);
        return mView;
    }

    @Override
    public void exit() {
        // TODO animate exit
        if (mParentView != null) {
            mParentView.removeView(mView);
            mParentView = null;
            mView = null;
        }
    }

    protected abstract void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView);
}

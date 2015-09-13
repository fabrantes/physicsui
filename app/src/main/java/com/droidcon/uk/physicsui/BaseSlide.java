package com.droidcon.uk.physicsui;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by fabrantes on 06/09/2015.
 */
public abstract class BaseSlide implements Slide, ViewTreeObserver.OnGlobalLayoutListener {

    public static final int UNKNOWN_SLIDE_IDX = -1;
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

    @NonNull
    public Resources getResources() {
        return mContext.getResources();
    }

    @NonNull
    public Context getContext() {
        return mContext;
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

    @Override
    public boolean stepTo(int stepIdx, boolean animate) {
        final boolean outOfBounds = stepIdx < 0 || stepIdx >= getStepCount();
        if (outOfBounds) {
            return false;
        }

        onStepTo(stepIdx, animate);
        setStepIdx(stepIdx);
        return true;
    }

    protected abstract void onStepTo(int stepIdx, boolean animate);

    @NonNull
    @Override
    public View enter(@NonNull ViewGroup viewGroup) {
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(mLayoutId, viewGroup, false);
            mParentView = viewGroup;
            viewGroup.addView(mView);
            onSlideInflated(mView, mParentView);
        }
        mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        stepTo(mStepIdx == UNKNOWN_SLIDE_IDX ? 0 : mStepIdx);
        return mView;
    }

    @Override
    public void exit() {
        // TODO animate exit
        mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (mParentView != null) {
            mParentView.removeView(mView);
            mParentView = null;
            mView = null;
        }
    }

    @Override
    public void onGlobalLayout() { }

    protected abstract void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView);
}

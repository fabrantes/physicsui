package com.droidcon.uk.physicsui;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fabrantes on 06/09/2015.
 */
public class PresentationControllerImpl implements PresentationController, View.OnKeyListener, View.OnClickListener {

    private final Presentation mPresentation;
    private final ViewGroup mViewGroup;
    private int mSlideIndex;

    public PresentationControllerImpl(@NonNull Presentation presentation, @NonNull ViewGroup viewGroup) {
        mPresentation = presentation;
        mSlideIndex = -1;

        mViewGroup = viewGroup;
        mViewGroup.setOnClickListener(this);
        mViewGroup.setOnKeyListener(this);
    }

    @NonNull
    @Override
    public Slide nextStep() {
        return step(1);
    }

    @NonNull
    @Override
    public Slide previousStep() {
        return step(-1);
    }

    @NonNull
    private /* eventually create SlideStep class */ Slide step(int stepDelta) {
        final Slide currentSlide = mPresentation.getSlide(mSlideIndex);
        final int stepIdx = currentSlide.getStepIdx() + stepDelta;
        if (!currentSlide.stepTo(stepIdx)) {
            moveToSlide(stepDelta > 0 ? mSlideIndex + 1 : mSlideIndex - 1);
        }
        return mPresentation.getSlide(mSlideIndex);
    }

    @NonNull
    @Override
    public Slide nextSlide() {
        return moveToSlide(mSlideIndex + 1);
    }

    @NonNull
    @Override
    public Slide previousSlide() {
        return moveToSlide(mSlideIndex - 1);
    }

    @NonNull
    private Slide moveToSlide(int slideIdx) {
        int nextSlideIdx = Math.min(0, Math.max(mPresentation.slideCount() - 1, slideIdx));
        if (nextSlideIdx == mSlideIndex) {
            return mPresentation.getSlide(mSlideIndex);
        }

        final Slide currentSlide = mPresentation.getSlide(mSlideIndex);
        final Slide nextSlide = mPresentation.getSlide(nextSlideIdx);
        mSlideIndex = nextSlideIdx;
        if (currentSlide != null) {
            currentSlide.exit();
        }
        nextSlide.enter(mViewGroup);
        return nextSlide;
    }

    @Override
    public void onClick(View v) {
        nextStep();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        nextStep();
        return true;
    }
}

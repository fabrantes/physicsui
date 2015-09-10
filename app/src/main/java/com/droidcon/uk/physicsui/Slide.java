package com.droidcon.uk.physicsui;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fabrantes on 06/09/2015.
 */
public interface Slide {

    Slide NONE = new Slide() {
        @Override
        public boolean prevStep() {
            return false;
        }

        @Override
        public boolean nextStep() {
            return false;
        }

        @Override
        public boolean stepTo(int stepIdx) {
            return stepTo(stepIdx, true);
        }

        @Override
        public boolean stepTo(int stepIdx, boolean animate) {
            return false;
        }

        @Override
        public int getStepCount() {
            return 0;
        }

        @Override
        public int getStepIdx() {
            return 0;
        }

        @NonNull
        @Override
        public View enter(@NonNull ViewGroup slideContainerView) {
            // TODO figure out this non null thing here (i.e. avoid allocating a new View)
            return new View(slideContainerView.getContext());
        }

        @Override
        public void exit() { }
    };

    boolean prevStep();

    boolean nextStep();

    boolean stepTo(int stepDelta);

    boolean stepTo(int stepDelta, boolean animate);

    int getStepCount();

    int getStepIdx();

    @NonNull View enter(@NonNull ViewGroup slideContainerView);
    void exit();


}

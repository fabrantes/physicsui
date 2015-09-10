package com.droidcon.uk.physicsui;

import android.support.annotation.NonNull;

/**
 * Created by fabrantes on 06/09/2015.
 */
public interface PresentationController {
    @NonNull
    Slide nextStep();

    @NonNull
    Slide previousStep();

    @NonNull
    Slide nextSlide();

    @NonNull
    Slide previousSlide();
}

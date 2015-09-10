package com.droidcon.uk.physicsui;

import android.support.annotation.NonNull;

/**
 * Created by fabrantes on 07/09/2015.
 */
public interface Presentation {
    int slideCount();

    @NonNull
    Slide getSlide(int mSlideIndex);
}

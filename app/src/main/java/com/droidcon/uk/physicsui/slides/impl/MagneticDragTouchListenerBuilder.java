package com.droidcon.uk.physicsui.slides.impl;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

public class MagneticDragTouchListenerBuilder {
    @NonNull private Iterable<View> targetViews = new ArrayList<>();
    private int minMagneticDistance;

    @NonNull
    public MagneticDragTouchListenerBuilder setTargetViews(@NonNull Iterable<View> targetViews) {
        this.targetViews = targetViews;
        return this;
    }

    @NonNull
    public MagneticDragTouchListenerBuilder setMinMagneticDistance(int minMagneticDistance) {
        this.minMagneticDistance = minMagneticDistance;
        return this;
    }

    @NonNull
    public MagneticDragTouchFilter build() {
        return new MagneticDragTouchFilter(targetViews, minMagneticDistance);
    }
}

package com.droidcon.uk.physicsui.slides.params;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.droidcon.uk.physicsui.R;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class SlideDemoMeaningfulParams extends SlideDemoParams {

    private static final float DEFAULT_FREQ = 10.455f; /* in rad/s  -- 230.2 k*/
    private static final float DEFAULT_BOUNCINESS = .5f;

    public SlideDemoMeaningfulParams(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    @Override
    public int getLabel1StringId() {
        return R.string.speed_freq;
    }

    @Override
    public int getLabel2StringId() {
        return R.string.bounciness_zeta;
    }

    @Override
    protected float getDefaultValue1() {
        return DEFAULT_FREQ;
    }

    @Override
    public int valueToProgress1(float value) {
        final int min = (int) (2 * Math.PI);
        final int max = (int) (8 * Math.PI);
        return (int) (1000 * (value - min) / (max - min));
    }

    @Override
    public float progressToValue1(float progress) {
        final int min = (int) (2 * Math.PI);
        final int max = (int) (8 * Math.PI);
        return min + progress * (max - min) / 1000;
    }

    @Override
    public float value1ToStiffness(float value, float mass, float value2) {
        final float dampingFactor = 1 - value2;
        return value * value / (1 - dampingFactor * dampingFactor) * mass;
    }

    @Override
    protected float getDefaultValue2() {
        return DEFAULT_BOUNCINESS;
    }

    @Override
    public int valueToProgress2(float value) {
        final float min = .01f;
        final float max = 1f;
        return (int) (1000 * (max - value - min) / (max - min));
    }

    @Override
    public float progressToValue2(float progress) {
        final float min = .01f;
        final float max = 1f;
        return min + progress * (max - min) / 1000;
    }

    @Override
    public float value2ToDamping(float value, float mass, float value1) {
        final float stiffness = value1ToStiffness(value1, mass, value);
        return (float) (2 * (1 - value) * Math.sqrt(mass * stiffness));
    }
}

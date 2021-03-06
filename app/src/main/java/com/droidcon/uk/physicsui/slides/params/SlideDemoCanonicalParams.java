package com.droidcon.uk.physicsui.slides.params;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.droidcon.uk.physicsui.R;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class SlideDemoCanonicalParams extends SlideDemoParams {

    private static final float DEFAULT_STIFFNESS = 230.2f;
    private static final float DEFAULT_DAMPING = 22f;

    public SlideDemoCanonicalParams(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    @Override
    public int getLabel1StringId() {
        return R.string.stiffness_k;
    }

    @Override
    public int getLabel2StringId() {
        return R.string.damping_c;
    }

    protected float getDefaultValue1() {
        return DEFAULT_STIFFNESS;
    }

    @Override
    public int valueToProgress1(float value) {
        final int min = 100;
        final int max = 1000;
        return (int) (1000 * (value - min) / (max - min));
    }

    @Override
    public float progressToValue1(float progress) {
        final int min = 100;
        final int max = 1000;
        return min + progress * (max - min) / 1000;
    }

    @Override
    public float value1ToStiffness(float value, float mass, float value2) {
        return value;
    }

    @Override
    protected float getDefaultValue2() {
        return DEFAULT_DAMPING;
    }

    @Override
    public int valueToProgress2(float value) {
        final int min = 1;
        final int max = 50;
        return (int) (1000 * (value - min) / (max - min));
    }

    @Override
    public float progressToValue2(float progress) {
        final int min = 1;
        final int max = 50;
        return min + progress * (max - min) / 1000;
    }

    @Override
    public float value2ToDamping(float value, float mass, float value1) {
        return value;
    }
}

package com.droidcon.uk.physicsui.slides.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

class MagneticDragTouchFilter implements View.OnTouchListener {
    private final int[] mLocOnScreen = new int[2];
    private final int mMinMagneticDistance;
    @NonNull private Iterable<View> mTargetViews;
    @Nullable private Listener mListener;

    @NonNull
    public static MagneticDragTouchListenerBuilder builder() {
        return new MagneticDragTouchListenerBuilder();
    }

    MagneticDragTouchFilter(
            @NonNull Iterable<View> targetViews,
            int minMagneticDistance) {
        mTargetViews = targetViews;
        mMinMagneticDistance = minMagneticDistance;
    }

    @NonNull
    public MagneticDragTouchFilter setListener(@Nullable Listener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final View demoContainer = (View) v.getParent(); // TODO fix how we access the parent offset
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN: {
                if (mListener != null) {
                    mListener.pnBeganDragging();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mListener != null) {
                    mListener.onFinishDragging();
                }
                break;
            }
            default: {
                final float targetX, targetY;
                final View draggableItem = v;
                final View targetView = getTargetView(event, draggableItem);

                if (targetView == draggableItem) {
                    // convert full-screen, centered coords (from getRaw*) to local coords
                    final float tLeft = event.getRawX() - demoContainer.getLeft() - draggableItem.getWidth() / 2;
                    final float tTop = event.getRawY() - demoContainer.getTop() - draggableItem.getHeight() / 2;
                    targetX = tLeft - draggableItem.getLeft();
                    targetY = tTop - draggableItem.getTop();
                } else {
                    targetX = targetView.getLeft() - draggableItem.getLeft();
                    targetY = targetView.getTop() - draggableItem.getTop();
                }
                if (mListener != null) {
                    mListener.onDragTargetUpdate(targetX, targetY);
                }
                break;
            }
        }
        return true;
    }

    @NonNull
    private View getTargetView(@NonNull MotionEvent event, @NonNull View draggableItem) {
        int minDistance = Integer.MAX_VALUE;
        View view = draggableItem;
        for (final View targetView : mTargetViews) {
            targetView.getLocationOnScreen(mLocOnScreen);
            final float targetCenterX = mLocOnScreen[0] + targetView.getWidth() / 2;
            final float targetCenterY = mLocOnScreen[1] + targetView.getHeight() / 2;
            final float distance = Math.abs(event.getRawX() - targetCenterX) +
                    Math.abs(event.getRawY() - targetCenterY);
            if (distance < minDistance && distance < mMinMagneticDistance) {
                minDistance = (int) distance;
                view = targetView;
            }
        }
        return view;
    }

    public interface Listener {
        void pnBeganDragging();
        void onFinishDragging();
        void onDragTargetUpdate(float x, float y);
    }
}

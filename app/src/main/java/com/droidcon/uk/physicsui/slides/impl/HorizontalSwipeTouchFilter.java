package com.droidcon.uk.physicsui.slides.impl;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

class HorizontalSwipeTouchFilter implements View.OnTouchListener {
    private final int[] mLocOnScreen = new int[2];
    private final Point mParentOffset = new Point();
    @NonNull final PointF mLastDownPoint = new PointF();
    @Nullable private Listener mListener;

    @NonNull
    public HorizontalSwipeTouchFilter setListener(@Nullable Listener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getParent() instanceof View) {
            final View parent = (View) v.getParent();
            parent.getLocationOnScreen(mLocOnScreen);
            mParentOffset.set(mLocOnScreen[0], mLocOnScreen[1]);
        }
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN: {
                if (mListener != null) {
                    mListener.onBeganDragging();
                }
                mLastDownPoint.set(event.getX(), event.getY());
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
                final float tLeft = event.getRawX() - mParentOffset.x - mLastDownPoint.x - v.getLeft();
                if (mListener != null) {
                    mListener.onDragTargetUpdate(tLeft, 0);
                }
                break;
            }
        }
        return true;
    }

    public interface Listener {
        void onBeganDragging();
        void onFinishDragging();
        void onDragTargetUpdate(float x, float y);
    }
}

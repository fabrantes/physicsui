package com.droidcon.uk.physicsui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.droidcon.uk.physicsui.R;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;

/**
 * Created by fabrantes on 27/09/2015.
 */
public class SpringPlotter extends View implements SpringListener {
    private static final float STROKE_WIDTH_FRACTION = .002f;
    private static final float PLOT_STROKE_WITH_MULTIPLIER = 4;
    private static final float GRAPH_TIME_MILLIS = 1000;
    private static final float GRAPH_OVERSHOOT_MARGIN = .4f;
    private int mPrimaryColorDark;
    private float mStrokeWidth;
    private long mSpringStartTime;
    @NonNull private RectF mGraphBoundingBox = new RectF();
    @NonNull private Paint mGraphAxisPaint;
    @NonNull private Paint mGraphLinePaint;
    @NonNull private Paint mGraphBitmapPaint;
    @Nullable private Bitmap mGraphBitmap;
    @Nullable private Canvas mGraphBitmapCanvas;
    @Nullable private Spring mSpring;
    @NonNull private SpringState mLastSpringState = new SpringState();
    @NonNull private SpringState mCurrentSpringState = new SpringState();
    @NonNull private Path mPath = new Path();

    public SpringPlotter(Context context) {
        this(context, null);
    }

    public SpringPlotter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringPlotter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        mPrimaryColorDark = typedValue.data;
        mGraphBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGraphAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGraphAxisPaint.setColor(mPrimaryColorDark);
        mGraphAxisPaint.setStrokeCap(Paint.Cap.ROUND);
        mGraphLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGraphLinePaint.setColor(mPrimaryColorDark);
        mGraphLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mGraphLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStrokeWidth = STROKE_WIDTH_FRACTION * Math.min(getWidth(), getHeight());
        final float strokeWidthMargin = mStrokeWidth * .5f + 1;

        mGraphAxisPaint.setStrokeWidth(mStrokeWidth);
        mGraphLinePaint.setStrokeWidth(mStrokeWidth * PLOT_STROKE_WITH_MULTIPLIER);

        mGraphBoundingBox.set(
                strokeWidthMargin,
                strokeWidthMargin,
                getWidth() - strokeWidthMargin,
                getHeight() - strokeWidthMargin);

        // Re-create our bitmap cache for drawing the graph points, if needed.
        if (mGraphBitmap == null ||
                mGraphBitmap.getWidth() != Math.floor(mGraphBoundingBox.width()) ||
                mGraphBitmap.getHeight() != Math.floor(mGraphBoundingBox.height())) {
            if (mGraphBitmap != null) {
                mGraphBitmap.recycle();
            }
            mGraphBitmap = Bitmap.createBitmap(
                    (int) Math.floor(mGraphBoundingBox.width()),
                    (int) Math.floor(mGraphBoundingBox.height()),
                    Bitmap.Config.ARGB_8888);
            mGraphBitmapCanvas = new Canvas(mGraphBitmap);
        }
    }

    public void setSpring(@Nullable Spring spring) {
        if (mSpring != null) {
            mSpring.removeListener(this);
        }

        mSpringStartTime = SystemClock.elapsedRealtime();
        mLastSpringState = new SpringState();
        if (mGraphBitmap != null) {
            mGraphBitmap = makeFadedCopyOf(mGraphBitmap);
            mGraphBitmapCanvas.setBitmap(mGraphBitmap);
        }
        spring.addListener(this);
        mSpring = spring;
    }

    @NonNull
    private Bitmap makeFadedCopyOf(@NonNull Bitmap origBitmap) {
        final Canvas canvas = new Canvas(origBitmap);
        final Paint paint = new Paint();
        paint.setAlpha(0x20);
        final Bitmap fadedCopyBitmap = Bitmap.createBitmap(
                origBitmap.getWidth(), origBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(fadedCopyBitmap);
        canvas.drawBitmap(origBitmap, 0, 0, paint);
        origBitmap.recycle();
        return fadedCopyBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGraphPoints(canvas);
        drawGraphAxis(canvas);
    }

    private void drawGraphPoints(@NonNull Canvas canvas) {
        canvas.drawBitmap(mGraphBitmap, mGraphBoundingBox.left, mGraphBoundingBox.top, mGraphBitmapPaint);
    }

    private void drawGraphAxis(@NonNull Canvas canvas) {
        // Y axis
        canvas.drawLine(
                mGraphBoundingBox.left, mGraphBoundingBox.bottom,
                mGraphBoundingBox.left, mGraphBoundingBox.top,
                mGraphAxisPaint);

        // X axis
        canvas.drawLine(
                mGraphBoundingBox.left, mGraphBoundingBox.bottom,
                mGraphBoundingBox.right, mGraphBoundingBox.bottom,
                mGraphAxisPaint);
    }

    private void updateBitmapGraph(@NonNull SpringState currentSpringState, @NonNull SpringState previousSpringState) {
        final float graphMaxValue = 1 + GRAPH_OVERSHOOT_MARGIN;
        final double previousTime = previousSpringState.getTime();
        final double currentTime = currentSpringState.getTime();
        final float dt = (float) (currentTime - previousTime);

        final double value = currentSpringState.getValue();
        final double velocity = currentSpringState.getVelocity();
        final double previousValue = previousSpringState.getValue();
        final double previousVelocity = previousSpringState.getVelocity();

        final float previousX = (float) (previousTime / GRAPH_TIME_MILLIS) * mGraphBitmap.getWidth();
        final float currentX = (float) ((currentTime / GRAPH_TIME_MILLIS) * mGraphBitmap.getWidth());
        final float previousY = (float) ((1 - previousValue / graphMaxValue) * (mGraphBitmap.getHeight()));
        final float currentY = (float) ((1 - value / graphMaxValue) * mGraphBitmap.getHeight());

        mPath.reset();
        mPath.moveTo(previousX, previousY);
        mPath.cubicTo(
                previousX + (currentX - previousX) * .1f, (float) (previousY - previousVelocity * dt * .1f),
                currentX - (currentX - previousX) * .1f, (float) (currentY + velocity * dt * .1f),
                currentX, currentY);
        mGraphBitmapCanvas.drawPath(mPath, mGraphLinePaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        if (mGraphBitmap == null) {
            // we're not ready to draw yet, just discard the update.
            return;
        }
        mCurrentSpringState.loadFrom(spring, mSpringStartTime);
        updateBitmapGraph(mCurrentSpringState, mLastSpringState);
        final SpringState currentSpringState = mLastSpringState;
        mLastSpringState = mCurrentSpringState;
        mCurrentSpringState = currentSpringState;
        postInvalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) { }

    @Override
    public void onSpringActivate(Spring spring) { }

    @Override
    public void onSpringEndStateChange(Spring spring) {
        final Spring changedSpring = spring;
        setSpring(changedSpring);
    }

    private static class SpringState {
        private double mValue;
        private double mVelocity;
        private long mTime;

        public long getTime() {
            return mTime;
        }

        public void setTime(long time) {
            this.mTime = time;
        }

        public double getValue() {
            return mValue;
        }

        public void setValue(double value) {
            this.mValue = value;
        }

        public double getVelocity() {
            return mVelocity;
        }

        public void setVelocity(double velocity) {
            this.mVelocity = velocity;
        }

        public void loadFrom(@NonNull Spring spring, long springStartTime) {
            setTime(SystemClock.elapsedRealtime() - springStartTime);
            setValue(spring.getCurrentValue());
            setVelocity(spring.getVelocity());
        }
    }
}

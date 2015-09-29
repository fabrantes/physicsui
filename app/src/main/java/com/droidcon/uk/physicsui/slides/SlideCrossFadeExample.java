package com.droidcon.uk.physicsui.slides;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidcon.uk.physicsui.BaseSlide;
import com.droidcon.uk.physicsui.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fabrantes on 13/09/2015.
 */
public class SlideCrossFadeExample extends BaseSlide {

    @Bind(R.id.code_text) TextView mCodeTextView;

    public SlideCrossFadeExample(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void onStepTo(int stepIdx, boolean animate) { }

    @Override
    protected void onSlideInflated(@NonNull View view, @NonNull ViewGroup parentView) {
        ButterKnife.bind(this, parentView);

        mCodeTextView.setText(
                "private void crossfade() {\n" +
                "\n" +
                "            // Set the content view to 0% opacity but visible, so that it is visible\n" +
                "            // (but fully transparent) during the animation.\n" +
                "            mContentView.setAlpha(0f);\n" +
                "            mContentView.setVisibility(View.VISIBLE);\n" +
                "\n" +
                "            // Animate the content view to 100% opacity, and clear any animation\n" +
                "            // listener set on the view.\n" +
                "            mContentView.animate()\n" +
                "                .alpha(1f)\n" +
                "                .setDuration(mShortAnimationDuration)\n" +
                "                .setListener(null);\n" +
                "\n" +
                "            // Animate the loading view to 0% opacity. After the animation ends,\n" +
                "            // set its visibility to GONE as an optimization step (it won't\n" +
                "            // participate in layout passes, etc.)\n" +
                "            mLoadingView.animate()\n" +
                "                .alpha(0f)\n" +
                "                .setDuration(mShortAnimationDuration)\n" +
                "                .setListener(new AnimatorListenerAdapter() {\n" +
                "                    @Override\n" +
                "                    public void onAnimationEnd(Animator animation) {\n" +
                "                        mLoadingView.setVisibility(View.GONE);\n" +
                "                    }\n" +
                "                });\n" +
                "}");
    }

    @Override
    public int getStepCount() {
        return 1;
    }
}

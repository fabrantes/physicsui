package com.droidcon.uk.physicsui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DroidconUkPresentation mPresentation;
    private PresentationControllerImpl mSlideController;

    @Bind(R.id.container) ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRootView.setOnClickListener(this);
        mPresentation = new DroidconUkPresentation(this);
        mSlideController = new PresentationControllerImpl(mPresentation, mRootView);
    }

    @Override
    public void onBackPressed() {
        // avoid leaving the activity
        mSlideController.previousStep();
    }

    @Override
    public void onClick(View v) {
        mSlideController.nextStep();
    }
}

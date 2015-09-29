package com.droidcon.uk.physicsui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.droidcon.uk.physicsui.slides.SlideCrossFadeExample;
import com.droidcon.uk.physicsui.slides.paramsdemo.SlideDemo;
import com.droidcon.uk.physicsui.slides.paramsdemo.SlideDemoCanonicalParams;
import com.droidcon.uk.physicsui.slides.theory.SlideTheory;
import com.droidcon.uk.physicsui.slides.theory.SlideTheory2;
import com.droidcon.uk.physicsui.slides.title.SlideTitle;
import com.droidcon.uk.physicsui.slides.whysprings.SlideWhySprings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class DroidconUkPresentation implements Presentation {

    @NonNull List<Slide> mSlides;

    public DroidconUkPresentation(@NonNull Context context) {
        mSlides = new ArrayList<>();
        mSlides.add(new SlideTitle(context, R.layout.slide_title));
        mSlides.add(new SlideWhySprings(context, R.layout.slide_why_springs));
        mSlides.add(new SlideTheory(context, R.layout.slide_theory));
        mSlides.add(new SlideTheory2(context, R.layout.slide_theory2));
        mSlides.add(new SlideDemo(context, R.layout.slide_demo));
        mSlides.add(new SlideDemoCanonicalParams(context, R.layout.slide_demo_canonical_params));
        mSlides.add(new SlideCrossFadeExample(context, R.layout.slide_cross_fade_ex));
    }

    @Override
    public int slideCount() {
        return mSlides.size();
    }

    @NonNull
    @Override
    public Slide getSlide(int index) {
        return index < 0 || index >= mSlides.size() ? Slide.NONE : mSlides.get(index);
    }
}

package com.droidcon.uk.physicsui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.droidcon.uk.physicsui.slides.impl.SlideDemoDragHotCorners;
import com.droidcon.uk.physicsui.slides.impl.SlideDemoRevertDrag;
import com.droidcon.uk.physicsui.slides.impl.SlideDemoSwipeToDelete;
import com.droidcon.uk.physicsui.slides.impl.SlideImplementation;
import com.droidcon.uk.physicsui.slides.impl.SlideImplementationOptions;
import com.droidcon.uk.physicsui.slides.impl.SlideIntegration;
import com.droidcon.uk.physicsui.slides.params.SlideDemo;
import com.droidcon.uk.physicsui.slides.params.SlideDemoCanonicalParams;
import com.droidcon.uk.physicsui.slides.params.SlideDemoMeaningfulParams;
import com.droidcon.uk.physicsui.slides.params.SlideMeaningfulParams;
import com.droidcon.uk.physicsui.slides.params.SlideMeaningfulParamsTheory;
import com.droidcon.uk.physicsui.slides.theory.SlideTheory;
import com.droidcon.uk.physicsui.slides.theory.SlideTheory2;
import com.droidcon.uk.physicsui.slides.title.SlideTitle;
import com.droidcon.uk.physicsui.slides.whysprings.SlideWhySprings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fabrantes on 08/09/2015.
 */
public class DroidconUkPresentation implements Presentation {

    @NonNull List<Slide> mSlides;

    public DroidconUkPresentation(@NonNull Context context) {
        mSlides = new ArrayList<>();
        mSlides.addAll(getDemoSlides(context));
    }

    @NonNull
    private Collection<? extends Slide> getDemoSlides(@NonNull Context context) {
        final List<Slide> slides = new ArrayList<>();
        mSlides.add(new SlideDemoCanonicalParams(context, R.layout.slide_demo_params));
        mSlides.add(new SlideDemoMeaningfulParams(context, R.layout.slide_demo_params));
        mSlides.add(new SlideDemoDragHotCorners(context, R.layout.slide_drag_hot_corners));
        mSlides.add(new SlideDemoRevertDrag(context, R.layout.slide_revert_drag));
        mSlides.add(new SlideDemoSwipeToDelete(context, R.layout.slide_swipe_to_del));
        return slides;
    }

    @NonNull
    private Collection<? extends Slide> getFullSlideDeck(@NonNull Context context) {
        final List<Slide> slides = new ArrayList<>();
        mSlides.add(new SlideTitle(context, R.layout.slide_title));
        mSlides.add(new SlideWhySprings(context, R.layout.slide_why_springs));
        mSlides.add(new SlideTheory(context, R.layout.slide_theory));
        mSlides.add(new SlideTheory2(context, R.layout.slide_theory2));
        mSlides.add(new SlideDemo(context, R.layout.slide_demo));
        mSlides.add(new SlideDemoCanonicalParams(context, R.layout.slide_demo_params));
        mSlides.add(new SlideMeaningfulParams(context, R.layout.slide_meaningful_params));
        mSlides.add(new SlideMeaningfulParamsTheory(context, R.layout.slide_meaningful_params_theory));
        mSlides.add(new SlideDemoMeaningfulParams(context, R.layout.slide_demo_params));
        mSlides.add(new SlideImplementation(context, R.layout.slide_impl));
        mSlides.add(new SlideImplementationOptions(context, R.layout.slide_impl_options));
        mSlides.add(new SlideDemoDragHotCorners(context, R.layout.slide_drag_hot_corners));
        mSlides.add(new SlideIntegration(context, R.layout.slide_integration));
        return slides;
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

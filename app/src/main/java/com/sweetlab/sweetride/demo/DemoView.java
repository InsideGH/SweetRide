package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sweetlab.sweetride.EngineView;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.math.Vec4;

/**
 * Demo gl surface view. Will create the demo application.
 */
public class DemoView extends EngineView {
    /**
     * GL application.
     */
    private DemoApplication2 mDemoApplication;

    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected UserApplication createUserApplication() {
        mDemoApplication = new DemoApplication2(getContext());
        return mDemoApplication;
    }

    @Override
    protected Vec4 getBackgroundColor() {
        return new Vec4(0.0f,0.0f, 0.0f, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDemoApplication.onTouchEvent(event);
    }
}

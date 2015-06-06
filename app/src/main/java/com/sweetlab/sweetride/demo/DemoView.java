package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.util.AttributeSet;

import com.sweetlab.sweetride.EngineView;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.math.Vec4;

/**
 * Demo gl surface view. Will create the demo application.
 */
public class DemoView extends EngineView {
    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected UserApplication createUserApplication() {
        return new DemoApplication();
    }

    @Override
    protected Vec4 getBackgroundColor() {
        return new Vec4(0.5f, 0.5f, 0.5f, 1);
    }
}

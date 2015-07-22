package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.util.AttributeSet;

import com.sweetlab.sweetride.EngineView;
import com.sweetlab.sweetride.UserApplication;

/**
 * Demo gl surface view. Will create the demo application.
 */
public class DemoView extends EngineView {
    /**
     * GL application.
     */
    private DemoApplication mDemoApplication;

    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected UserApplication createUserApplication() {
        mDemoApplication = new DemoApplication(getContext());
        return mDemoApplication;
    }
}

package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.util.AttributeSet;

import com.sweetlab.sweetride.EngineView;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.demo.game.GameApplication;

/**
 * Demo gl surface view. Will create the demo application.
 */
public class MainContentView extends EngineView {
    /**
     * GL application.
     */
//    private DemoApplication mDemoApplication;
    private GameApplication mGameApplication;

    public MainContentView(Context context) {
        super(context);
    }

    public MainContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected UserApplication createUserApplication() {
//        mDemoApplication = new DemoApplication(getContext());
//        return mDemoApplication;
        mGameApplication = new GameApplication(getContext());
        return mGameApplication;
    }
}

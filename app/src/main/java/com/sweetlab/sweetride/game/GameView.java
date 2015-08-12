package com.sweetlab.sweetride.game;

import android.content.Context;
import android.util.AttributeSet;

import com.sweetlab.sweetride.EngineView;
import com.sweetlab.sweetride.UserApplication;

/**
 * A game view.
 */
public class GameView extends EngineView {
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected UserApplication createUserApplication() {
        return null;
    }
}

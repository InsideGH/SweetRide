package com.sweetlab.sweetride;

import android.app.Application;

import com.sweetlab.sweetride.engine.EngineLogs;
import com.sweetlab.sweetride.logger.Logger;

/**
 * The main application.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(getString(R.string.log_tag), getApplicationContext());
        Logger.addCategory(EngineLogs.class);
    }
}

package com.sweetlab.sweetride.Util;

import android.opengl.GLES20;

import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Collection of used render settings.
 */
public class BackendRenderSettingsUtil {

    /**
     * Get the default (most common) backend render setting. Grey background.
     *
     * @param width  Viewport width.
     * @param height Viewport height.
     * @return Backend render setting.
     */
    public static BackendRenderSettings getDefaultBlack(int width, int height) {
        BackendRenderSettings settings = new BackendRenderSettings();
        settings.setViewPort(0, 0, width, height);
        settings.setClearColor(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
        settings.setClear(0, GLES20.GL_COLOR_BUFFER_BIT);
        return settings;
    }

    /**
     * Get the default (most common) backend render setting. Grey background.
     *
     * @param width  Viewport width.
     * @param height Viewport height.
     * @return Backend render setting.
     */
    public static BackendRenderSettings getDefaultGrey(int width, int height) {
        BackendRenderSettings settings = new BackendRenderSettings();
        settings.setViewPort(0, 0, width, height);
        settings.setClearColor(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
        settings.setClear(0, GLES20.GL_COLOR_BUFFER_BIT);
        return settings;
    }

    /**
     * Get the default (most common) backend render setting. Green background.
     *
     * @param width  Viewport width.
     * @param height Viewport height.
     * @return Backend render setting.
     */
    public static BackendRenderSettings getDefaultGreen(int width, int height) {
        BackendRenderSettings settings = new BackendRenderSettings();
        settings.setViewPort(0, 0, width, height);
        settings.setClearColor(new float[]{0.0f, 1.0f, 0.0f, 1.0f});
        settings.setClear(0, GLES20.GL_COLOR_BUFFER_BIT);
        return settings;
    }

}

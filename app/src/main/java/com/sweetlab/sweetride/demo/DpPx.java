package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Dp to pixel and vice versa converter.
 */
public class DpPx {
    /**
     * Convert dp to pixels.
     *
     * @param context Android context.
     * @param dp      Dp value.
     * @return Pixel value.
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    /**
     * Convert pixel to dp.
     *
     * @param context Android context.
     * @param px      Pixel value.
     * @return Dp value.
     */
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }
}

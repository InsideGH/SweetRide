package com.sweetlab.sweetride.context.Util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

/**
 * Collection of bitmap related primitives.
 */
public class BitmapTestUtil {
    /**
     * Create a bitmap with some color.
     *
     * @param config Bitmap config.
     * @return A bitmap.
     */
    public static Bitmap createQuadColorBitmap(Bitmap.Config config) {
        return Bitmap.createBitmap(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, 2, 2, config);
    }

    /**
     * Create a chess like bitmap with random colors.
     *
     * @param config Bitmap config.
     * @return A bitmap.
     */
    public static Bitmap createChessColorBitmap(Bitmap.Config config) {
        int values[] = new int[10 * 10];
        int p = 0;
        Random random = new Random(10);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int grey = random.nextInt(256);
                values[p] = Color.argb(255, grey, grey, grey);
                p++;
            }
        }
        return Bitmap.createBitmap(values, 10, 10, config);
    }
}

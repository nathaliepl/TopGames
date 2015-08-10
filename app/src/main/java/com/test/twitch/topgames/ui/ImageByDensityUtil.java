package com.test.twitch.topgames.ui;

import android.content.Context;
import android.util.DisplayMetrics;

import com.test.twitch.topgames.model.ImageSizes;

/**
 * Created by nathalielima on 8/9/15.
 */
public class ImageByDensityUtil {
    public static String bindImage(Context context, ImageSizes imageSizes) {
        int density = context.getResources().getDisplayMetrics().densityDpi;

        if (density == DisplayMetrics.DENSITY_LOW) {
            return imageSizes.getSmall();
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            return imageSizes.getMedium();
        } else {
            return imageSizes.getLarge();
        }

    }
}

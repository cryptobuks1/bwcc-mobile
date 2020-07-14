package com.inovasialfatih.klinikbwcc.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class HRes {
    public static int color(Context context, int resourceId) {
        return ContextCompat.getColor(context, resourceId);
    }

    public static Drawable drawable(Context context, int resourceId) {
        return ContextCompat.getDrawable(context, resourceId);
    }

    public static String string(Context context, int resourceId) {
        return context.getString(resourceId);
    }
}

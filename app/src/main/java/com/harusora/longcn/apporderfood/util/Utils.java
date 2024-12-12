package com.harusora.longcn.apporderfood.util;

import android.content.Context;

public class Utils {

    public static int getResourceIdFromDrawable(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }
}

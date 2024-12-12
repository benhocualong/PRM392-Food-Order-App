package com.harusora.longcn.apporderfood.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static int getResourceIdFromDrawable(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }

    public static String dateToString(Date date){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}

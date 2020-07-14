package com.inovasialfatih.klinikbwcc.views;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import com.onurkaganaldemir.ktoastlib.KToast;

public class Toasty {

    public static void success (Context context, String message){
        KToast.successToast((Activity) context, message, Gravity.BOTTOM, KToast.LENGTH_SHORT);
    }

    public static void error(Context context, String message) {
        KToast.errorToast((Activity) context, message, Gravity.BOTTOM, KToast.LENGTH_SHORT);
    }

    public static void warning(Context context, String message) {
        KToast.warningToast((Activity) context, message, Gravity.BOTTOM, KToast.LENGTH_SHORT);
    }

    public static void info(Context context, String message) {
        KToast.infoToast((Activity) context, message, Gravity.BOTTOM, KToast.LENGTH_SHORT);
    }
}

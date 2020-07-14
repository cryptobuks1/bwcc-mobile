package com.inovasialfatih.klinikbwcc.extensions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Navigator {
    private Context context;

    public Navigator(Context context){
        this.context = context;
    }

    public void navigate(Class goClass){
        go(goClass, null, false, false);
    }

    public void navigate(Class goClass, boolean isActivityFinish){
        go(goClass, null, isActivityFinish, false);
    }

    public void navigate(Class goClass, Bundle bundle){
        go(goClass, bundle, false, false);
    }

    public void navigate(Class goClass, Bundle bundle, boolean isActivityFinish){
        go(goClass, bundle, isActivityFinish, false);
    }

    public void navigateClearAll(Class goClass, boolean clearPreviousActivity){
        go(goClass, null, false, clearPreviousActivity);
    }

    public void navigateClearAll(Class goClass, Bundle bundle, boolean clearPreviousActivity){
        go(goClass, bundle, false, clearPreviousActivity);
    }

    private void go(Class goClass, Bundle bundle, boolean isActivityFinish, boolean clearPreviousActivity){
        Intent i = new Intent(context, goClass);

        if(clearPreviousActivity)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        if(bundle != null) {
            i.putExtras(bundle);
        }

        context.startActivity(i);

        if(isActivityFinish)
            ((Activity)context).finish();
    }
}

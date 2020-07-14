package com.inovasialfatih.klinikbwcc.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;


public class Loadie {
    private AlertDialog dialog;

     public Loadie(Context context, String title, String desc) {
         initialize(context, title, desc);
     }

    public Loadie(Context context, String title) {
        initialize(context, title, "");
    }

    public Loadie(Context context, int intResTitle, int intResDesc) {
        String title = HRes.string(context, intResTitle);
        String desc = HRes.string(context, intResDesc);
        initialize(context, title, desc);
    }

     public AlertDialog build() {
         return dialog;
     }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

     private void initialize(Context context, String title, String desc) {
         View content = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null);
         ((TextView)content.findViewById(R.id.progress_title)).setText(title);

         if(!TextUtils.isEmpty(desc)) {
             content.findViewById(R.id.progress_desc).setVisibility(View.VISIBLE);
             ((TextView) content.findViewById(R.id.progress_desc)).setText(desc);
         } else content.findViewById(R.id.progress_desc).setVisibility(View.GONE);

         dialog = new AlertDialog.Builder(context)
                 .setView(content)
                 .create();
     }
}

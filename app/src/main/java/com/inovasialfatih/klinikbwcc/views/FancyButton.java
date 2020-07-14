package com.inovasialfatih.klinikbwcc.views;

import android.content.Context;
import android.util.AttributeSet;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

public class FancyButton extends MaterialFancyButton {
    public FancyButton(Context context) {
        super(context);
        setCustomTextFont(HRes.string(context, R.string.font_path_bold));
    }

    public FancyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTextFont(HRes.string(context, R.string.font_path_bold));
    }
}

package com.inovasialfatih.klinikbwcc.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.extensions.Navigator;
import com.inovasialfatih.klinikbwcc.services.Gate;

public class BaseFragment extends Fragment {
    protected Context _ctx;
    protected Navigator _nav;
    protected Gate _gate;
    protected Cooky _cooky;

    protected void init(Context context){
        _ctx = context;
        _nav = new Navigator(_ctx);
        _cooky = new Cooky(_ctx);
        _gate = new Gate(_ctx);
    }
}

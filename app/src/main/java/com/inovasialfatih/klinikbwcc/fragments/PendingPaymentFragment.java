package com.inovasialfatih.klinikbwcc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inovasialfatih.klinikbwcc.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPaymentFragment extends Fragment {


    public PendingPaymentFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        PendingPaymentFragment fragment = new PendingPaymentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pending_payment, container, false);

        return root;
    }

}

package com.inovasialfatih.klinikbwcc.extensions;

import android.content.Context;

import com.google.gson.Gson;
import com.inovasialfatih.klinikbwcc.model.RefItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SvRes {
    public static String USERNAME = "username";
    public static String EMAIL = "email";

    private Context context;
    private Cooky cooky;

    public SvRes(Context context){
        this.context = context;
        cooky = new Cooky(context);
    }

    public void addRefs(String name, List<RefItem> refs) {
        Gson gson = new Gson();
        String strJson = gson.toJson(refs);

        cooky.addString(name, strJson);
    }

    public ArrayList<RefItem> getRefs(String name) {
        List<RefItem> items;

        String strJson = cooky.getString(name);
        Gson gson = new Gson();
        RefItem[] arrItems = gson.fromJson(strJson,
                RefItem[].class);

        if(arrItems != null) {
            items = Arrays.asList(arrItems);
            items = new ArrayList<>(items);
        } else {
            items = new ArrayList<>();
        }

        return (ArrayList<RefItem>) items;
    }
}

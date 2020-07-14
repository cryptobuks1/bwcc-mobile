package com.inovasialfatih.klinikbwcc.model;

public class RefItem {
    private String id;
    private String name;

    public RefItem() {
    }

    public RefItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

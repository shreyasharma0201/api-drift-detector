package com.apidrift.model;

public class Drift {
    public final String type;
    public final String method;
    public final String path;

    public Drift(String type, String method, String path) {
        this.type = type;
        this.method = method;
        this.path = path;
    }

    @Override
    public String toString() {
        return type + "  " + method + " " + path;
    }
}

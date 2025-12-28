package com.apidrift.model;

public class Drift {
    public final String type;
    public final String endpoint;

    public Drift(String type, String endpoint) {
        this.type = type;
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return type + "  " + endpoint;
    }
}

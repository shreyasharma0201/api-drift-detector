package com.apidrift.model;

public class Drift {
    public final String type;
    public final Severity severity;
    public final String method;
    public final String path;
    public final String detail;

    public Drift(String type, Severity severity, String method, String path, String detail) {
        this.type = type;
        this.severity = severity;
        this.method = method;
        this.path = path;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return severity + "  " + type + "  " + method + " " + path + " -> " + detail;
    }
}

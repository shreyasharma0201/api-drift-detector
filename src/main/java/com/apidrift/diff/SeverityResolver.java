package com.apidrift.diff;

import com.apidrift.model.Severity;

public class SeverityResolver {

    public static Severity resolve(String driftType) {
        return switch (driftType) {
            case "METHOD_REMOVED",
                 "FIELD_REMOVED",
                 "FIELD_TYPE_CHANGED",
                 "RESPONSE_STATUS_REMOVED",
                 "RESPONSE_FIELD_REMOVED",
                 "RESPONSE_FIELD_TYPE_CHANGED" -> Severity.BREAKING;

            case "METHOD_ADDED",
                 "FIELD_ADDED" -> Severity.NON_BREAKING;

            default -> Severity.RISKY;
        };
    }
}

package com.apidrift.diff;

import com.apidrift.model.Drift;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import java.util.*;

public class ResponseSchemaDiffer {

    public static List<Drift> diff(OpenAPI oldApi, OpenAPI newApi) {
        List<Drift> drifts = new ArrayList<>();

        oldApi.getPaths().forEach((path, oldItem) -> {
            PathItem newItem = newApi.getPaths().get(path);
            if (newItem == null) return;

            compare("GET", path, oldItem.getGet(), newItem.getGet(), drifts);
            compare("POST", path, oldItem.getPost(), newItem.getPost(), drifts);
            compare("PUT", path, oldItem.getPut(), newItem.getPut(), drifts);
            compare("DELETE", path, oldItem.getDelete(), newItem.getDelete(), drifts);
        });

        return drifts;
    }

    private static void compare(
            String method,
            String path,
            Operation oldOp,
            Operation newOp,
            List<Drift> drifts
    ) {
        if (oldOp == null || newOp == null) return;

        Map<String, ApiResponse> oldResp = oldOp.getResponses();
        Map<String, ApiResponse> newResp = newOp.getResponses();

        // status code removed
        for (String status : oldResp.keySet()) {
            if (!newResp.containsKey(status)) {
                drifts.add(new Drift(
                        "RESPONSE_STATUS_REMOVED",
                        SeverityResolver.resolve("RESPONSE_STATUS_REMOVED"),
                        method,
                        path,
                        "status " + status
                ));
            }
        }

        // schema comparison
        for (String status : oldResp.keySet()) {
            if (!newResp.containsKey(status)) continue;

            Schema<?> oldSchema = getSchema(oldResp.get(status));
            Schema<?> newSchema = getSchema(newResp.get(status));

            if (oldSchema == null || newSchema == null) continue;

            Map<String, Schema> oldProps = props(oldSchema);
            Map<String, Schema> newProps = props(newSchema);

            for (String field : oldProps.keySet()) {
                if (!newProps.containsKey(field)) {
                    drifts.add(new Drift(
                            "RESPONSE_STATUS_REMOVED",
                            SeverityResolver.resolve("RESPONSE_STATUS_REMOVED"),
                            method,
                            path,
                            "status " + status
                    ));
                } else {
                    String o = oldProps.get(field).getType();
                    String n = newProps.get(field).getType();
                    if (!Objects.equals(o, n)) {
                        drifts.add(new Drift(
                                "RESPONSE_STATUS_REMOVED",
                                SeverityResolver.resolve("RESPONSE_STATUS_REMOVED"),
                                method,
                                path,
                                "status " + status
                        ));
                    }
                }
            }
        }
    }

    private static Schema<?> getSchema(ApiResponse resp) {
        if (resp.getContent() == null) return null;
        var media = resp.getContent().get("application/json");
        return media == null ? null : media.getSchema();
    }

    private static Map<String, Schema> props(Schema<?> schema) {
        return schema.getProperties() == null
                ? Map.of()
                : schema.getProperties();
    }
}

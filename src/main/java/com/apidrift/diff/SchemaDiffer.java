package com.apidrift.diff;

import com.apidrift.model.Drift;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;

import java.util.*;

public class SchemaDiffer {

    public static List<Drift> diff(OpenAPI oldApi, OpenAPI newApi) {
        List<Drift> drifts = new ArrayList<>();

        oldApi.getPaths().forEach((path, oldItem) -> {
            PathItem newItem = newApi.getPaths().get(path);
            if (newItem == null) return;

            compareOperation("POST", path, oldItem.getPost(), newItem.getPost(), drifts);
            compareOperation("PUT", path, oldItem.getPut(), newItem.getPut(), drifts);
        });

        return drifts;
    }

    private static void compareOperation(
            String method,
            String path,
            Operation oldOp,
            Operation newOp,
            List<Drift> drifts
    ) {
        if (oldOp == null || newOp == null) return;

        Schema<?> oldSchema = getRequestSchema(oldOp);
        Schema<?> newSchema = getRequestSchema(newOp);

        if (oldSchema == null || newSchema == null) return;

        Map<String, Schema> oldProps = safeProps(oldSchema);
        Map<String, Schema> newProps = safeProps(newSchema);

        for (String field : oldProps.keySet()) {
            if (!newProps.containsKey(field)) {
                drifts.add(new Drift(
                        "FIELD_REMOVED",
                        method,
                        path,
                        "request.body." + field
                ));
            } else {
                String oldType = oldProps.get(field).getType();
                String newType = newProps.get(field).getType();
                if (!Objects.equals(oldType, newType)) {
                    drifts.add(new Drift(
                            "FIELD_TYPE_CHANGED",
                            method,
                            path,
                            field + ": " + oldType + " -> " + newType
                    ));
                }
            }
        }
    }

    private static Schema<?> getRequestSchema(Operation op) {
        RequestBody body = op.getRequestBody();
        if (body == null || body.getContent() == null) return null;

        var media = body.getContent().get("application/json");
        if (media == null) return null;

        return media.getSchema();
    }

    private static Map<String, Schema> safeProps(Schema<?> schema) {
        return schema.getProperties() == null
                ? Map.of()
                : schema.getProperties();
    }
}

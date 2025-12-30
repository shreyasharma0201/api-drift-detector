package com.apidrift.diff;

import com.apidrift.model.Drift;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.*;

public class EndpointDiffer {

    public static List<Drift> diff(OpenAPI oldApi, OpenAPI newApi) {
        List<Drift> result = new ArrayList<>();

        Set<String> oldEndpoints = extract(oldApi);
        Set<String> newEndpoints = extract(newApi);

        // 1. Check for REMOVALS
        for (String e : oldEndpoints) {
            if (!newEndpoints.contains(e)) {
                String[] parts = e.split(" ", 2);
                result.add(new Drift("METHOD_REMOVED", parts[0], parts[1], "Endpoint is no longer present in the new spec"));
            }
        } // End of first loop

        // 2. Check for ADDITIONS
        for (String e : newEndpoints) {
            if (!oldEndpoints.contains(e)) {
                String[] parts = e.split(" ", 2);
                result.add(new Drift("METHOD_ADDED", parts[0], parts[1], "New endpoint detected in the updated spec"));
            }
        } // End of second loop

        return result;
    }

    private static Set<String> extract(OpenAPI api) {
        Set<String> endpoints = new HashSet<>();

        api.getPaths().forEach((path, item) -> {
            add(endpoints, "GET", item.getGet(), path);
            add(endpoints, "POST", item.getPost(), path);
            add(endpoints, "PUT", item.getPut(), path);
            add(endpoints, "DELETE", item.getDelete(), path);
            add(endpoints, "PATCH", item.getPatch(), path);
        });

        return endpoints;
    }

    private static void add(Set<String> set, String method, Object op, String path) {
        if (op != null) {
            set.add(method + " " + path);
        }
    }
}

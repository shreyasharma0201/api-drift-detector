// diff/EndpointDiffer.java
package com.apidrift.diff;

import com.apidrift.model.Drift;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.*;

public class EndpointDiffer {

    public static List<Drift> diff(OpenAPI oldApi, OpenAPI newApi) {
        List<Drift> result = new ArrayList<>();

        Set<String> oldPaths = oldApi.getPaths().keySet();
        Set<String> newPaths = newApi.getPaths().keySet();
        System.out.println(oldPaths);
        System.out.println(newPaths);

        for (String p : oldPaths) {
            if (!newPaths.contains(p)) {
                result.add(new Drift("REMOVED", p));
            }
        }

        for (String p : newPaths) {
            if (!oldPaths.contains(p)) {
                result.add(new Drift("ADDED", p));
            }
        }

        return result;
    }
}

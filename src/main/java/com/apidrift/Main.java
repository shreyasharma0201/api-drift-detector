
// Main.java
package com.apidrift;

import com.apidrift.diff.EndpointDiffer;
import com.apidrift.loader.OpenApiLoader;
import io.swagger.v3.oas.models.OpenAPI;

public class Main {
    public static void main(String[] args) {
        OpenAPI oldApi = OpenApiLoader.load("baseline.yaml");
        OpenAPI newApi = OpenApiLoader.load("current.yaml");

        EndpointDiffer.diff(oldApi, newApi)
                .forEach(System.out::println);
    }
}

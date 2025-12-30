package com.apidrift;

import com.apidrift.diff.EndpointDiffer;
import com.apidrift.loader.OpenApiLoader;
import com.apidrift.diff.SchemaDiffer;
import com.apidrift.diff.ResponseSchemaDiffer;

public class Main {
    public static void main(String[] args) {
        var oldApi = OpenApiLoader.load("baseline.yaml");
        var newApi = OpenApiLoader.load("current.yaml");

        EndpointDiffer.diff(oldApi, newApi)
                .forEach(System.out::println);
        SchemaDiffer.diff(oldApi, newApi)
                .forEach(System.out::println);
        ResponseSchemaDiffer.diff(oldApi, newApi)
                .forEach(System.out::println);

    }
}

package com.apidrift.loader;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class OpenApiLoader {

    public static OpenAPI load(String path) {

        System.out.println("Loading OpenAPI from " + path);
        System.out.println(new OpenAPIV3Parser().read(path));
        return new OpenAPIV3Parser().read(path);
    }
}
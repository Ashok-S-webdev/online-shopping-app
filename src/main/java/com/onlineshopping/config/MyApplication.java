package com.onlineshopping.config;



import java.util.Collections;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class MyApplication extends ResourceConfig{
    public MyApplication() {
        packages("com.onlineshopping");
        register(MultiPartFeature.class);

        OpenAPI openAPI = new OpenAPI()
            .info(new Info()
                .title("Online Shopping App API")
                .description("API documentation for Online Shopping App with Jersey")
                .version("1.0")
            )
            .addServersItem(new Server().url("http://localhost:8080/online-shopping-app"));

        SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration()
            .openAPI(openAPI)
            .resourcePackages(Collections.singleton("com.onlineshopping.api"));

        OpenApiResource openApiResource = new OpenApiResource();
        openApiResource.setOpenApiConfiguration(swaggerConfiguration);

        register(openApiResource);
        register(CorsFilter.class);
    }
}

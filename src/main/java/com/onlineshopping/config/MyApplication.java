package com.onlineshopping.config;


import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class MyApplication extends ResourceConfig{
    public MyApplication() {
        packages("com.onlineshopping");
        register(MultiPartFeature.class);
    }
}
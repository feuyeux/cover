package com.mine.config;

import com.mine.resource.ApiResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig{
    public JerseyConfig(){
        register(ApiResource.class);
    }
}

package com.example.app4;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String home() {
        return "{\"app\":\"app4\",\"framework\":\"quarkus\",\"message\":\"App 4 is running\"}";
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public String health() {
        return "{\"status\":\"UP\"}";
    }
}

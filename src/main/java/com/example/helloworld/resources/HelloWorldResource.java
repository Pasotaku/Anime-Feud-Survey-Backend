package com.example.helloworld.resources;

import com.example.helloworld.core.Saying;
import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;
import com.pasotaku.jwt.JWT;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final JWT jwt = new JWT("secretkey");

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    @Path("/getJWT")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJWTToken(@QueryParam("jwt") String jwt) {
        return Response.status(Response.Status.OK).entity(this.jwt.createJWT(jwt)).build();
    }

    @GET
    @Timed
    @Path("/getValidJWT")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValidJWTToken(@QueryParam("jwt") String jwt) {
        String decoded = this.jwt.decodeJWT(jwt);
        if(decoded == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.OK).entity(decoded).build();

    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.or(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }
}
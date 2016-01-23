package com.pasotaku.animefeud.resources;

import com.codahale.metrics.annotation.Timed;
import com.pasotaku.animefeud.core.AuthenticationService;
import com.pasotaku.animefeud.core.Saying;
import com.pasotaku.animefeud.core.User;
import com.pasotaku.jwt.AuthToken;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final AuthToken jwt = new AuthToken();
    private final AuthenticationService authenticationService = new AuthenticationService();

    public AuthenticationResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @POST
    @Timed
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response login(@FormParam("username") String username, @FormParam("password") String password,@FormParam("userIdentity") String userIdentity) throws AuthenticationException {
        com.google.common.base.Optional<User> authenticated =  authenticationService.authenticate(new BasicCredentials(username, password));
        if(!authenticated.isPresent()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String userIdentityTranscoded = this.jwt.createJwt(userIdentity);
        Cookie myCookie = new Cookie("authentication", this.jwt.createJwt(userIdentityTranscoded));
        NewCookie newCookie = new NewCookie(myCookie);
        return Response.status(Response.Status.OK).entity(userIdentityTranscoded).cookie(newCookie).build();

    }

    @GET
    @Timed
    @Path("/getJWT")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getJWTToken(@QueryParam("jwt") String jwt) {
        String encoded = this.jwt.createJwt(jwt);
        if(encoded == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.OK).entity(encoded).build();
    }



    @GET
    @Timed
    @Path("/getValidJWT")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getValidJWTToken(@QueryParam("jwt") String jwt) {
        String decoded = this.jwt.decodeJwt(jwt);
        System.out.println(jwt);
        if(decoded == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.OK).entity(decoded).build();

    }
}
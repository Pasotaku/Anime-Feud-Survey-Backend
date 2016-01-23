package com.pasotaku.animefeud.core;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.google.common.base.Optional;


/**
 * Created by johnlight on 1/22/16.
 */
public class AuthenticationService implements Authenticator<BasicCredentials, User> {

    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword()) && "test".equals(credentials.getUsername())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.absent();
    }
}

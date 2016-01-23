package com.pasotaku.animefeud.core;

/**
 * Created by johnlight on 1/22/16.
 */
import java.security.Principal;

public class User implements Principal {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }
}
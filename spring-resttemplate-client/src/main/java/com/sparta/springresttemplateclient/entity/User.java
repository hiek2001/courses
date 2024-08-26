package com.sparta.springresttemplateclient.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

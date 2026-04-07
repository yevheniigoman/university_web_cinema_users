package com.iasaweb.cinemausers.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with name '" + username + "' was not found");
    }
}

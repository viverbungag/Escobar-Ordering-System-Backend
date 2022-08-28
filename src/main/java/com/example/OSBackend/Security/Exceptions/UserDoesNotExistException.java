package com.example.OSBackend.Security.Exceptions;

public class UserDoesNotExistException extends RuntimeException{

    public UserDoesNotExistException(){
        super("The user does not exist");
    }
}

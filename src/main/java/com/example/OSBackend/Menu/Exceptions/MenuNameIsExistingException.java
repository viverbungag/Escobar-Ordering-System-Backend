package com.example.OSBackend.Menu.Exceptions;

public class MenuNameIsExistingException extends RuntimeException{

    public MenuNameIsExistingException(String name){
        super(String.format("This name %s is already existing", name));
    }
}

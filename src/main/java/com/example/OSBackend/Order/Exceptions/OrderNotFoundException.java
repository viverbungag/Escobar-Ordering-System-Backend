package com.example.OSBackend.Order.Exceptions;

import java.time.LocalDateTime;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(LocalDateTime time){
        super(String.format("Could not find order with time: %s", time));
    }
}

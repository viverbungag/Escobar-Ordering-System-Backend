package com.example.OSBackend.Order.Exceptions;

public class OrderDiscountOutOfRangeException extends RuntimeException{

    public OrderDiscountOutOfRangeException(){
        super("Discount value should be between 0 to 100");
    }
}

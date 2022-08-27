package com.example.OSBackend.Pagination.Exceptions;

public class PageOutOfBoundsException extends RuntimeException{

    public PageOutOfBoundsException(Integer currentPageNo){
        super(String.format("You inputted an invalid page number: %s", currentPageNo));
    }
}

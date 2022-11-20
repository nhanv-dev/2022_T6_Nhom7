package com.service;

public interface ISendMailError {
    void sendError(String error,String path, String... receiver);
}

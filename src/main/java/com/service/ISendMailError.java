package com.service;

public interface ISendMailError {
    void sendError(String error, String... receiver);
}

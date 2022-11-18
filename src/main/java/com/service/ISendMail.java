package com.service;

public interface ISendMail {
    void sendError(String error, String... receiver);
}

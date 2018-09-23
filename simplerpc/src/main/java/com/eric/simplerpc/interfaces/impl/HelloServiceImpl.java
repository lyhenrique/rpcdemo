package com.eric.simplerpc.interfaces.impl;

import com.eric.simplerpc.interfaces.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return "hello" + msg;
    }
}

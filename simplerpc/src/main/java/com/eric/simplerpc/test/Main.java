package com.eric.simplerpc.test;

import com.eric.simplerpc.client.RpcProxy;
import com.eric.simplerpc.interfaces.HelloService;
import com.eric.simplerpc.interfaces.impl.HelloServiceImpl;

/**
 * 测试类
 */
public class Main {

    public static void main(String[] args) {
        HelloService service = RpcProxy.create(HelloServiceImpl.class);
        String msg = service.hello("my rpc");
    }
}

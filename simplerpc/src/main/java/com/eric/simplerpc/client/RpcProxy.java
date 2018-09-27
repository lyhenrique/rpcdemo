package com.eric.simplerpc.client;

import com.eric.simplerpc.client.invocation.MyInvocationHandler;
import java.lang.reflect.Proxy;

/**
 * rpc客户端 通过代理机制来触发远程调用
 */
public class RpcProxy {

    @SuppressWarnings("unchecked")
    public static <T> T create(final Object target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), new MyInvocationHandler(target));
    }

}

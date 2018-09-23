package com.eric.simplerpc.server.handler;

import com.eric.simplerpc.entity.ClassInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理服务端操作的类
 */
public class InvokerHandler extends ChannelInboundHandlerAdapter {

    public static Map<String, Object> classMap = new ConcurrentHashMap<String,Object>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //由于有对象解析器,发送接收的信息全部可以直接强转为ClassInfo对象
        ClassInfo classInfo = (ClassInfo) msg;
        Object clazz = null;
        if (!classMap.containsKey(classInfo.getClassName())) {
            clazz = Class.forName(classInfo.getClassName()).newInstance();
            classMap.put(classInfo.getClassName(), clazz);
        } else {
            clazz = classMap.get(classInfo.getClassName());
        }

        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

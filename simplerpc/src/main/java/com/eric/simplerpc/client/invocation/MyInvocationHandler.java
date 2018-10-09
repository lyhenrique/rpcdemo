package com.eric.simplerpc.client.invocation;

import com.eric.simplerpc.client.handler.MessageDecoder;
import com.eric.simplerpc.client.handler.MessageEncoder;
import com.eric.simplerpc.client.handler.ResultHandler;
import com.eric.simplerpc.entity.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 自定义反射调用处理类
 */
public class MyInvocationHandler implements InvocationHandler{

    private Object target;

    private Class clazz;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    public MyInvocationHandler(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ClassInfo classInfo = new ClassInfo();
        if (target != null) {
            classInfo.setClassName(target.getClass().getName());
        } else {
            classInfo.setClassName(clazz.getName());
        }
        classInfo.setMethodName(method.getName());
        classInfo.setObjects(args);
        classInfo.setTypes(method.getParameterTypes());
        classInfo.setRequestId(UUID.randomUUID().toString());

        final ResultHandler resultHandler = new ResultHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
//                                    .addLast(new MessageEncoder())
//                                    .addLast(new MessageDecoder())
                                    /**消息体的编码序列化*/
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    .addLast(resultHandler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            /**消息传输*/
            channelFuture.channel().writeAndFlush(classInfo).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

        return resultHandler.getResponse();
    }
}

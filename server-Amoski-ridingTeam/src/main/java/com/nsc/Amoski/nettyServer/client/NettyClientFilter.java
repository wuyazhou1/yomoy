package com.nsc.Amoski.nettyServer.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.ArrayList;
import java.util.List;


public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        /*
         * 解码和编码，应和服务端一致
         * */
        /*ph.addLast(new StringDecoder());
        ph.addLast( new StringEncoder());*/
        //ph.addLast(new NettySelfEncoder());
        //ph.addLast("framedecoder",new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 4,0,4));
        ph.addLast( new NettyClientHandler()); //客户端的逻辑
    }

}


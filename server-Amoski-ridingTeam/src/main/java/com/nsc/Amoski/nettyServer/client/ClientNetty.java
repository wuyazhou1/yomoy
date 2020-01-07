package com.nsc.Amoski.nettyServer.client;

import com.nsc.Amoski.dto.NettyProtoDto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class ClientNetty {

    public static String host = "122.114.91.150";  //ip地址
    public static int port = 9000;          //端口

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) throws Exception {

        //NioEventLoopGroup可以看做是一个线程池,客户端只需要用来处理发送数据任务的NioEventLoopGroup即可
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)//指定Nio模式为Client模式
                .handler(new NettyClientFilter());
        ChannelFuture cf = b.connect(host,port);         //连接指定host:ip
        String str="Hello Netty";
//        cf.channel().write(Unpooled.copiedBuffer("Hello I am Client ".getBytes()));//write是写入缓冲区,
//        cf.channel().flush();             //flush缓冲数据,必须flush!! 或者使用writeAndFlush方法发送数据

        Scanner sc=new Scanner(System.in);
        while(!str.equals("quit")){
            byte[] bytes = str.getBytes();
            System.out.println("客户端发送数据:"+str+"============str:"+(str.length()+str)+">>>>>>>>>>");
            NettyProtoDto dto=new NettyProtoDto((byte)0xAB, (byte)0xCD, str.getBytes().length, str);
            ByteBuf buf = cf.channel().alloc().buffer();
            //buf.writeByte(bytes.length);
            buf.writeBytes(bytes);
            cf.channel().writeAndFlush(buf);
            str=sc.next();
        }
        cf.channel().closeFuture().sync();//异步监听,传输完毕才执行此代码,然后向下执行关闭操作
        group.shutdownGracefully();
        //star();
    }
}

package com.nsc.Amoski.nettyServer;

import com.nsc.Amoski.controller.DealNettyReq;
import com.nsc.Amoski.dto.NettyProtoDto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private static final int MAX_FRAME_LENGTH = 1024 * 1024 * 1024;
    /**
     * 线程数
     */
    private static final int LINEPROSESS=6;
    /**
     * 用来指定了请求等待队列的大小（多个请求,一个个处理,其他的等待）
     */
    private static final int SO_BACKLOG = 258;
    private static final boolean SO_KEEPALIVE = true;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    @Value("${netty.server.port}")
    public Integer port;

    @Autowired
    DealNettyReq dealNettyReq;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    private void startServer(){
        //服务端需要2个线程组  boss处理客户端连接  work进行客服端连接之后的处理
        EventLoopGroup boss = new NioEventLoopGroup(LINEPROSESS);
        EventLoopGroup work = new NioEventLoopGroup(LINEPROSESS);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            //服务器 配置
            bootstrap.group(boss,work).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline ph = socketChannel.pipeline();
                    //以$为分隔符
                    ByteBuf buf = Unpooled.copiedBuffer("\\r\\n".getBytes());
                    ph.addLast(new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, buf))
                            .addLast(new StringDecoder());
                    // 以("\n")为结尾分割的 解码器
                    //ph.addLast(new NettySelfDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP,false));
                    //ph.addLast("framedecoder",new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 4,0,4));
                    // 解码和编码，应和客户端一致
                    /*ph.addLast(new StringDecoder());
                    ph.addLast( new StringEncoder());*/

                    // HttpServerCodec：将请求和应答消息解码为HTTP消息
                    //ph.addLast("http-codec",new HttpServerCodec());
                    // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
                    //ph.addLast("aggregator",new HttpObjectAggregator(1024*1024*1024));
                    // ChunkedWriteHandler：向客户端发送HTML5文件
                    /*socketChannel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast(new HttpServerCodec());
                    socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));*/
                    //ph.addLast(new WebSocketServerProtocolHandler("/testWs"));
                    // 进行设置心跳检测
                    //socketChannel.pipeline().addLast(new IdleStateHandler(60,30,60*30, TimeUnit.SECONDS));
                    // 配置通道处理  来进行业务处理
                    //socketChannel.pipeline().addLast(new IdleStateHandler(5,5,60, TimeUnit.SECONDS));
                    ph.addLast(new MyChannelHandler(dealNettyReq));
                }
            }).option(ChannelOption.SO_BACKLOG,SO_BACKLOG).childOption(ChannelOption.SO_KEEPALIVE, SO_KEEPALIVE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.TCP_NODELAY, true)//禁用nagle算法
                    .childOption(ChannelOption.SO_LINGER, -1)//关闭Socket的延迟时间
                    .childOption(ChannelOption.SO_SNDBUF, SO_BACKLOG*SO_BACKLOG/2)
                    .childOption(ChannelOption.SO_RCVBUF, SO_BACKLOG*SO_BACKLOG)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(1, SO_BACKLOG*SO_BACKLOG/2, SO_BACKLOG*SO_BACKLOG));
            //绑定端口  开启事件驱动
            LOGGER.info("【服务器启动成功========端口："+port+"】");
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    //@PostConstruct()
    public void init(){
        //需要开启一个新的线程来执行netty server 服务器
        /*new Thread(new Runnable() {
            public void run() {
                startServer();
            }
        }).start();*/
        System.out.println(">>>>>...222");
        startServer();
    }

    public static void main(String [] arg){
        NettyServer sv=new NettyServer();
        sv.setPort(9000);
        sv.init();
    }
}

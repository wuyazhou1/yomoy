package com.nsc.Amoski.nettyServer;

import com.nsc.Amoski.controller.DealNettyReq;
import com.nsc.Amoski.dto.NettyJsonObj;
import com.nsc.Amoski.dto.SocketDealType;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyChannelHandler extends ChannelInboundHandlerAdapter  {


    private static final Logger log = LoggerFactory.getLogger(MyChannelHandler.class);

    DealNettyReq dealNettyReq;

    public MyChannelHandler(){}
    public MyChannelHandler(DealNettyReq dealNettyReq){
        this.dealNettyReq=dealNettyReq;
    }

    /**
     * 连接上服务器
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("【handlerAdded】====>"+ctx.channel().id());
        GlobalUserUtil.channels.add(ctx.channel());
    }
/*
    *//**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("【handlerRemoved】====>"+ctx.channel().id());
        GlobalUserUtil.channels.remove(ctx.channel());
        NettyJsonObj obj=new NettyJsonObj();
        obj.setType(SocketDealType.OFFLINE.getCode());
        dealNettyReq.teammateOffline(ctx,obj);
    }

    /**
     * 连接异常   需要关闭相关资源
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("【系统异常】======>"+cause.toString());
        GlobalUserUtil.channels.remove(ctx.channel());
        NettyJsonObj obj=new NettyJsonObj();
        obj.setType(SocketDealType.OFFLINE.getCode());
        dealNettyReq.teammateOffline(ctx,obj);
        ctx.channel().close().sync();
    }
    /**
     * 这里只要完成 flush
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>>>>>>>发送数据完成!!!!!!!");
        ctx.flush();
    }
    /**
     * 心跳丢失次数
     */
    private int counter = 0;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = null;
            try {
                event = (IdleStateEvent) evt;switch (event.state()){
                    case READER_IDLE:
                        // 空闲40s之后触发 (心跳包丢失)
                        if (counter >= 3) {
                            // 连续丢失3个心跳包 (断开连接)
                            ctx.channel().close().sync();
                            /*NettyJsonObj obj=new NettyJsonObj();
                            obj.setType(SocketDealType.OFFLINE.getCode());
                            dealNettyReq.teammateOffline(ctx,obj);*/
                            log.info("已与"+ctx.channel().remoteAddress()+"断开连接");
                        } else {
                            counter++;
                            log.info("丢失了第 " + counter + " 个心跳包");
                        }
                        break;
                    case WRITER_IDLE:
                        log.info("写空闲");
                        //eventType = "写空闲";
                        break;
                    case ALL_IDLE:
                        log.info("读写空闲");
                        //eventType ="读写空闲";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext cf, Object msg) throws Exception {
        /*ByteBuf buf=(ByteBuf) msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req);*/
        String body=msg.toString();
        if(body.indexOf("1001")>0){//传点
            //return;
        }
        if(body.indexOf("1003")>0){//心跳
            return;
        }
        log.info("server recive data!!!! : " + body);
        dealNettyReq.centerDealReq(cf,body);
    }
}

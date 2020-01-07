package com.nsc.Amoski.nettyServer.client;

import com.nsc.Amoski.dto.NettyProtoDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class NettySelfEncoder extends MessageToByteEncoder<NettyProtoDto> {
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyProtoDto msg, ByteBuf out) throws Exception {
        if(null == msg){
            throw new Exception("msg is null");
        }

        String body = msg.getContent();
        byte[] bodyBytes = body.getBytes(Charset.forName("utf-8"));
        out.writeByte(msg.getType());
        out.writeByte(msg.getFlag());
        out.writeInt(bodyBytes.length);
        out.writeBytes(bodyBytes);
    }
}



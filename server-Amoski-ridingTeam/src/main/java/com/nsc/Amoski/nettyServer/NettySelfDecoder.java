package com.nsc.Amoski.nettyServer;

import com.nsc.Amoski.dto.NettyProtoDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


public class NettySelfDecoder extends LengthFieldBasedFrameDecoder {

    //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte+int = 1+1+4 = 6
    private static final int HEADER_SIZE = 6;


    public NettySelfDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public NettySelfDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
    /**
     *
     * @param maxFrameLength  帧的最大长度
     * @param lengthFieldOffset length字段偏移的地址
     * @param lengthFieldLength length字段所占的字节长
     * @param lengthAdjustment 修改帧数据长度字段中定义的值，可以为负数 因为有时候我们习惯把头部记入长度,若为负数,则说明要推后多少个字段
     * @param initialBytesToStrip 解析时候跳过多少个长度
     * @param failFast 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异
     */
    public NettySelfDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    private byte [] currData;
    private int allLength;

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception("可读信息段比头部信息都小，你在逗我？");
        }
        //注意在读的过程中，readIndex的指针也在移动
        byte type = in.readByte();

        byte flag = in.readByte();

        int length = in.readInt();
        currData=new byte[length];
        //ByteBuf buf = in.readBytes(in.readableBytes());
        byte[] req = new byte[in.readableBytes()];
        allLength+=in.readableBytes();
        in.readBytes(req);
        NettyProtoDto customMsg =new NettyProtoDto(type,flag,length,"11111");
        /*if (in.readableBytes() < length) {
            allLength=1;
            //throw new Exception("body字段你告诉我长度是"+length+",但是真实情况是没有这么多，你又逗我？");
            System.arraycopy(req,0,currData,allLength-1,in.readableBytes());
        }
        if(allLength>=currData.length){
            String body = new String(currData, "UTF-8");
            customMsg=new NettyProtoDto(type,flag,length,body);
        }*/
        return customMsg;
    }

}

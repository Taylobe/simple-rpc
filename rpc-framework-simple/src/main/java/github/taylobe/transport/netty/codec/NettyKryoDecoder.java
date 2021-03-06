package github.taylobe.transport.netty.codec;

import github.taylobe.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义NettyKryo解码器
 * 负责处理"入站"信息，将消息格式转换成我们需要的业务对象
 */
@AllArgsConstructor
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(NettyKryoDecoder.class);

    private final Serializer serializer;
    private final Class<?> genericClass;
    // Netty传输的消息长度，也就是对象序列化后对应的字节数组的大小，存储在ByteBuf头部
    private static final int BODY_LENGTH = 4;

    /**
     * 解码byteBuf对象
     *
     * @param channelHandlerContext 解码器关联的channelHandlerContext对象
     * @param byteBuf               "入站"数据，byteBuf对象
     * @param list                  解码之后的数据对象需要添加到list数组里面
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        //1.byteBuf中写入的消息长度所占的字节数已经是4了，所以byteBuf的可读字节必须大于4
        if (byteBuf.readableBytes() >= BODY_LENGTH) {
            //2.标记当前readIndex的位置，以便后面重置readIndex的时候使用
            byteBuf.markReaderIndex();
            /**
             * 3.读取消息的长度
             * 注意：消息长度是encode的时候我们自己写入的，参见NettyKryoEncoder的encoder方法
             */
            int dataLength = byteBuf.readInt();
            //4.遇见不合理的情况，直接return
            if (dataLength < 0 || byteBuf.readableBytes() < 0) {
                logger.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            //5.如果可读字节数小于消息长度的话，说明消息不完整，重置readIndex
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                return;
            }
            //6.序列化
            byte[] bytes = new byte[dataLength];
            byteBuf.readBytes(bytes);
            //7.将bytes数组转换成我们需要的对象
            Object object = serializer.deserialize(bytes, genericClass);
            list.add(object);
            logger.info("successful decode ByteBuf to Object");
        }
    }
}

package github.taylobe.transport.netty;

import github.taylobe.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NettyKryo编码器
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        if (genericClass.isInstance(o)) {
            //1.将对象转换成byte
            byte[] bytes = serializer.serialize(o);
            //2.获取消息的长度
            int length = bytes.length;
            //3.写入消息对应的字节数组长度，writerIndex加4
            byteBuf.writeInt(length);
            //4.将字节数组写入byteBuf对象中
            byteBuf.writeBytes(bytes);
        }
    }
}

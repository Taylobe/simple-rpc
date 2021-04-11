package github.taylobe.transport.netty.codec;

import github.taylobe.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * 自定义NettyKryo编码器
 * 负责处理"出站"消息，将消息格式转换字节数组然后写入到字节数据的容器byteBuf对象中
 * 网络传输需要通过字节流来实现，byteBuf可以看作是netty提供的字节数据容器，使用它可以更方便地处理字节数据
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> genericClass;

    /**
     * 将对象转换为字节码然后写入到 ByteBuf 对象中
     *
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     */
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

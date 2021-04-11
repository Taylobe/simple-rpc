package github.taylobe.transport.netty.client;

import github.taylobe.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义客户端
 * ChannelHandler 处理服务端发过来的数据
 * <p>
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    /**
     * 读取服务端传输的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        try {
            logger.info(String.format("client receive msg : %s", msg));
            RpcResponse rpcResponse = (RpcResponse) msg;
            // 声明AttributeKey一个对象,类似map中的key
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcResponse.getRequestId());
            /**
             * 将服务端返回的结果保存到AttributeMap上
             * AttributeMap 可以看作是一个channel的共享数据源
             * key-AttributeKey value-Attribute
             */
            channelHandlerContext.channel().attr(key).set(rpcResponse);
            channelHandlerContext.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 处理客户端消息发生异常的时候被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        logger.error("client catch exception : ", throwable);
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
}

package github.taylobe.transport.netty;

import github.taylobe.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        try {
            RpcResponse rpcResponse = (RpcResponse) msg;
            logger.info(String.format("client receive msg : %s", rpcResponse));
            // 声明AttributeKey一个对象
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
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

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        logger.error("client catch exception");
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
}

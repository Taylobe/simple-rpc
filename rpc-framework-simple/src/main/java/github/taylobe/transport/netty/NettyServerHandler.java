package github.taylobe.transport.netty;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.registry.DefaultServiceRegistry;
import github.taylobe.registry.ServiceRegistry;
import github.taylobe.transport.RpcRequestHandle;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义服务端的 ChannelHandler 来处理客户端发过来的数据
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RpcRequestHandle rpcRequestHandle;
    private static ServiceRegistry serviceRegistry;

    static {
        rpcRequestHandle = new RpcRequestHandle();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            logger.info(String.format("server receive msg : %s", rpcRequest));
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = rpcRequestHandle.handle(rpcRequest, service);
            logger.info(String.format("server get result : %s", result));
            ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(RpcResponse.success(result));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        logger.error("server catch exception");
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
}

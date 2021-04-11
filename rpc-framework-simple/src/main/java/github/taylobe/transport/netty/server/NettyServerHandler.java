package github.taylobe.transport.netty.server;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.registry.DefaultServiceRegistry;
import github.taylobe.registry.ServiceRegistry;
import github.taylobe.transport.RpcRequestHandle;
import github.taylobe.utils.concurrent.ThreadPoolFactory;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * 自定义服务端的 ChannelHandler 来处理客户端发过来的数据
 * <p>
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RpcRequestHandle rpcRequestHandle;
    private static ServiceRegistry serviceRegistry;
    private static ExecutorService threadPool;

    static {
        rpcRequestHandle = new RpcRequestHandle();
        serviceRegistry = new DefaultServiceRegistry();
        threadPool = ThreadPoolFactory.createDefaultThreadPool("netty-server-handler-rpc-pool");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        threadPool.execute(() -> {
            logger.info(String.format("server handler message from client by thread : %s", Thread.currentThread().getName()));
            try {
                logger.info(String.format("server receive msg : %s", msg));
                RpcRequest rpcRequest = (RpcRequest) msg;
                String interfaceName = rpcRequest.getInterfaceName();
                //通过注册中心获取到目标类（客户端需要调用类）
                Object service = serviceRegistry.getService(interfaceName);
                //执行目标方法（客户端需要执行的方法），并且返回方法结果
                Object result = rpcRequestHandle.handle(rpcRequest, service);
                logger.info(String.format("server get result : %s", result));
                //返回方法执行的结果给客户端
                ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            } finally {
                //确保byteBuf被释放，不然有可能会有内存泄露问题
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        logger.error("server catch exception");
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
}

package github.taylobe.transport.netty.client;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.transport.ClientTransport;
import github.taylobe.utils.checker.RpcMessageChecker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * netty客户端传输类
 */
public class NettyClientTransport implements ClientTransport {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientTransport.class);
    private InetSocketAddress inetSocketAddress;

    public NettyClientTransport(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            Channel channel = ChannelProvider.get(inetSocketAddress);
            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        logger.info("client send message: {}", rpcRequest);
                    } else {
                        future.channel().close();
                        logger.error("Send failed:", future.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> rpcResponseAttributeKey = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(rpcResponseAttributeKey).get();
                logger.info("client get rpcResponse from channel:{}", rpcResponse);
                //校验 RpcResponse 和 RpcRequest
                RpcMessageChecker.check(rpcResponse, rpcRequest);
                result.set(rpcResponse.getDate());
            } else {
                System.exit(0);
            }
        } catch (InterruptedException exception) {
            logger.error("occur exception when send rpc message from client:", exception);
        }
        return result.get();
    }
}

package github.taylobe.transport.netty.client;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.serialize.kryo.KryoSerializer;
import github.taylobe.transport.RpcClient;
import github.taylobe.transport.netty.codec.NettyKryoDecoder;
import github.taylobe.transport.netty.codec.NettyKryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端，发送消息到服务端。
 * 接收服务端的返回的方法执行结果
 */
public class NettyRpcClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private String host;
    private int port;
    private static final Bootstrap bootStrap;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //初始化相关资源 例如EventLoopGroup、Bootstrap
    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootStrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootStrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        /*自定义序列化编解码器*/
                        //RpcResponse -> ByteBuf
                        socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        //ByteBuf -> RpcRequest
                        socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 发送消息到服务端
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture channelFuture = bootStrap.connect(host, port).sync();
            logger.info("client connect {}", host + ":" + port);
            Channel channel = channelFuture.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        logger.info(String.format("client send message : %s", rpcRequest.toString()));
                    } else {
                        logger.error("send failed : ", future.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getDate();
            }
        } catch (InterruptedException e) {
            logger.error("occur exception when connect server : ", e);
            e.printStackTrace();
        }
        return null;
    }
}

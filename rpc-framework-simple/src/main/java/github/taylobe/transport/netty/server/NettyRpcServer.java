package github.taylobe.transport.netty.server;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.serialize.kryo.KryoSerializer;
import github.taylobe.transport.netty.codec.NettyKryoDecoder;
import github.taylobe.transport.netty.codec.NettyKryoEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端，接收客户端消息
 * 根据客户端的消息调用相应的方法，并且返回结果给客户端
 */
public class NettyRpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);
    private final int port;
    private KryoSerializer kryoSerializer;

    public NettyRpcServer(int port) {
        this.port = port;
        kryoSerializer = new KryoSerializer();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            /*自定义序列化编解码器*/
                            //RpcRequest -> ByteBuf
                            socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            //ByteBuf -> RpcResponse
                            socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    //设置tcp缓冲区
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            //绑定端口，同步等待绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("occur exception when start server : ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

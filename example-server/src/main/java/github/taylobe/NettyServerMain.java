package github.taylobe;

import github.taylobe.registry.DefaultServiceRegistry;
import github.taylobe.transport.netty.server.NettyRpcServer;

/**
 * netty服务启动类
 */
public class NettyServerMain {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        // 手动注册
        defaultServiceRegistry.registry(helloService);
        NettyRpcServer nettyRpcServer = new NettyRpcServer(9999);
        nettyRpcServer.run();
    }
}

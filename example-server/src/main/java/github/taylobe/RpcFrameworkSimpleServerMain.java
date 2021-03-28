package github.taylobe;

import github.taylobe.registry.DefaultServiceRegistry;
import github.taylobe.remoting.socket.RpcServer;

/**
 * 服务端启动类
 */
public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        // 手动注册
        defaultServiceRegistry.registry(helloService);
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(9999);
    }
}

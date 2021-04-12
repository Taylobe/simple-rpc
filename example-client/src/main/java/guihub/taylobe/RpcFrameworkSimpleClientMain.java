package guihub.taylobe;

import github.taylobe.Hello;
import github.taylobe.HelloService;
import github.taylobe.transport.RpcClient;
import github.taylobe.transport.RpcClientProxy;
import github.taylobe.transport.socket.SocketRpcClient;

/**
 * 客户端启动类
 */
public class RpcFrameworkSimpleClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketRpcClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("hello", "rpc is coming"));
        System.out.println(hello);
    }
}

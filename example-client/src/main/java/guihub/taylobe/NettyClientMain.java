package guihub.taylobe;

import github.taylobe.Hello;
import github.taylobe.HelloService;
import github.taylobe.transport.RpcClient;
import github.taylobe.transport.RpcClientProxy;
import github.taylobe.transport.netty.NettyRpcClient;

public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("hi", "rpc is come"));
        System.out.println(hello);
    }
}

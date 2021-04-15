package guihub.taylobe;

import github.taylobe.Hello;
import github.taylobe.HelloService;
import github.taylobe.transport.ClientTransport;
import github.taylobe.transport.RpcClientProxy;
import github.taylobe.transport.netty.client.NettyClientTransport;

import java.net.InetSocketAddress;

public class NettyClientMain {
    public static void main(String[] args) {
        ClientTransport clientTransport = new NettyClientTransport(new InetSocketAddress("127.0.0.1", 9999));
        RpcClientProxy rpcClientProxy = new RpcClientProxy(clientTransport);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("hi", "rpc is come"));
        System.out.println("如果上面的调用卡住了，这里也不会调用类");
        helloService.hello(new Hello("hi", "rpc is coming"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is rpc is come".equals(hello);
    }
}

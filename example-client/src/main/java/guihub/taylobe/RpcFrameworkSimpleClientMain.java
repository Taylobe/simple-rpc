package guihub.taylobe;

import github.taylobe.Hello;
import github.taylobe.HelloService;
import github.taylobe.remoting.socket.RpcClientProxy;

/**
 * 客户端启动类
 */
public class RpcFrameworkSimpleClientMain {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("127.0.0.1", 9999);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("hello", "rpc is coming"));
        System.out.println(hello);
    }
}

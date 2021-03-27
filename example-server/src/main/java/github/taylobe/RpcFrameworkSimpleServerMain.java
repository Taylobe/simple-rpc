package github.taylobe;

/**
 * 服务端启动类
 */
public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(new HelloServiceImpl(), 9999);
        //todo 修改实现方式，通过map存放service，解决只能注册一个service
        System.out.println("后面的不会执行");
        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}

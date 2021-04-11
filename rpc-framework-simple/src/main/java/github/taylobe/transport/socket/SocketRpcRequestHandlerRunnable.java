package github.taylobe.transport.socket;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.registry.DefaultServiceRegistry;
import github.taylobe.registry.ServiceRegistry;
import github.taylobe.transport.RpcRequestHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketRpcRequestHandlerRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SocketRpcRequestHandlerRunnable.class);

    private Socket socket;
    private static RpcRequestHandle rpcRequestHandle;
    private static ServiceRegistry serviceRegistry;

    static {
        rpcRequestHandle = new RpcRequestHandle();
        serviceRegistry = new DefaultServiceRegistry();
    }

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        logger.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = rpcRequestHandle.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("occur exception : ", e);
        }
    }
}

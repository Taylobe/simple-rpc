package github.taylobe.remoting.socket;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.registry.ServiceRegistry;
import github.taylobe.remoting.RpcRequestHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcRequestHandleRunnable implements Runnable {

    public static final Logger logger = LoggerFactory.getLogger(RpcRequestHandleRunnable.class);

    private Socket socket;
    private RpcRequestHandle rpcRequestHandle;
    private ServiceRegistry serviceRegistry;

    public RpcRequestHandleRunnable(Socket socket, RpcRequestHandle rpcRequestHandle, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.rpcRequestHandle = rpcRequestHandle;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
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

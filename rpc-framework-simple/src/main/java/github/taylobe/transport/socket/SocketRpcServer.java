package github.taylobe.transport.socket;

import github.taylobe.utils.concurrent.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * rpc客户端代理
 */
public class SocketRpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-server-rpc-pool");
    }

    /**
     * 服务端主动注册服务
     * TODO 修改为注解然后扫描
     */
    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("server starts...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                logger.info("client connected");
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("occur IOException : ", e);
        }
    }
}

package github.taylobe.remoting.socket;

import github.taylobe.registry.ServiceRegistry;
import github.taylobe.remoting.RpcRequestHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * rpc服务端
 */
public class RpcServer {

    public static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    /**
     * 线程池参数
     */
    public static final int CORE_POOL_SIZE = 10;
    public static final int MAXIMUM_POOL_SIZE = 100;
    public static final int KEEP_ALIVE_TIME = 1;
    public static final int BLOCKING_QUEUE_CAPACITY = 100;
    private ExecutorService threadPool;

    private RpcRequestHandle rpcRequestHandle = new RpcRequestHandle();
    private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 初始化线程池
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
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
                threadPool.execute(new RpcRequestHandleRunnable(socket, rpcRequestHandle, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("occur IOException:", e);
        }
    }
}

package github.taylobe.registry;

import github.taylobe.enumeration.RpcErrorMessageEnum;
import github.taylobe.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册中心实现
 * 通过map保存服务信息
 * todo 后期可以通过zookeeper实现
 */
public class DefaultServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    /**
     * 接口名和服务的对应关系，todo 处理一个接口被多个实现类实现的情况
     * key : 服务/接口 名字
     * value : 服务
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * todo 修改为扫描注解注册
     * 将这个对象所有的实现的接口都注册进去
     */
    @Override
    public <T> void registry(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class clazz : interfaces) {
            serviceMap.put(clazz.getCanonicalName(), service);
        }
        logger.info("Add service : {} and interfaces : {}", serviceName, service.getClass().getInterfaces());
    }

    /**
     * 通过服务名获取服务
     */
    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}

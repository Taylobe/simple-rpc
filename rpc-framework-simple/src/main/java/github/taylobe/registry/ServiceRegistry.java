package github.taylobe.registry;

/**
 * 服务注册中心接口
 */
public interface ServiceRegistry {

    <T> void registry(T service);

    Object getService(String serviceName);

}

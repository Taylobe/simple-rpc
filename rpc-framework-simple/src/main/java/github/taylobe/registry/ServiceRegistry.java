package github.taylobe.registry;

/**
 * 服务注册
 */
public interface ServiceRegistry {
    <T> void registry(T service);

    Object getService(String serviceName);
}

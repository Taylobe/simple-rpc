package github.taylobe.serialize;

/**
 * 序列化接口
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object 序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的数组
     * @param clazz 类
     * @param <T>   反序列化的对象
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}

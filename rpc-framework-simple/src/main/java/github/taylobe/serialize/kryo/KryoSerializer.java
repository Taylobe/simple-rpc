package github.taylobe.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.exception.SerializeException;
import github.taylobe.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo序列化类
 * 优点 : 序列化效率高
 * 缺点 : 只兼容Java语言
 */
public class KryoSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * 因为kryo不是线程安全的，所以每个线程都应该有自己的kryo，input和output实例。
     * 使用ThreadLocal存放kryo对象，实现线程隔离。
     */
    private final ThreadLocal<Kryo> KryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        //是否关闭注册行为，默认值为true。关闭之后可能存在序列化问题，一般推荐设置为true。
        kryo.setReferences(true);
        //是否关闭循环引用，默认值为false。可以提高性能，但是一般不推荐设置为true。
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /**
     * 序列化
     *
     * @param object 序列化的对象
     * @return 字节数组
     */
    @Override
    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = KryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output, object);
            KryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("occur exception when serialize : ", e);
            throw new SerializeException("序列化失败");
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 序列化后的数组
     * @param clazz 类
     * @param <T>   反序列化的对象
     * @return
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = KryoThreadLocal.get();
            //byte -> object:从byte数组中反序列化出对象
            Object object = kryo.readObject(input, clazz);
            KryoThreadLocal.remove();
            return clazz.cast(object);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("occur exception when deserialize : ", e);
            throw new SerializeException("反序列化失败");
        }
    }
}

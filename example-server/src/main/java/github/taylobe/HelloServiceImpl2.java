package github.taylobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hello服务实现2
 */
public class HelloServiceImpl2 implements HelloService {

    public static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(Hello hello) {
        logger.info("HelloServiceImpl2收到 : {}", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        logger.info("HelloServiceImpl2返回 : {}", result);
        return result;
    }
}

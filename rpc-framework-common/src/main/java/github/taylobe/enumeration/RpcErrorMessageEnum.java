package github.taylobe.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Rpc错误信息枚举类型
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessageEnum {

    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_FOUND("找不到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口");

    private final String message;

}

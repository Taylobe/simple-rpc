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
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空");

    private final String message;

}

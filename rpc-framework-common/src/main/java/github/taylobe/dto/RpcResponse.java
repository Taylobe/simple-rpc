package github.taylobe.dto;

import github.taylobe.enumeration.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * Rpc响应类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;

    //响应码
    private Integer code;
    //响应信息
    public String message;
    //响应数据
    public T date;

    /**
     * 成功
     */
    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(RpcResponseCode.SUCCESS.getCode());
        if (null != data) {
            rpcResponse.setDate(data);
        }
        return rpcResponse;
    }

    /**
     * 失败
     */
    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(rpcResponseCode.getCode());
        rpcResponse.setMessage(rpcResponse.getMessage());
        return rpcResponse;
    }
}

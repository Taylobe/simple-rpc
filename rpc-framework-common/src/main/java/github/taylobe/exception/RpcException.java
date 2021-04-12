package github.taylobe.exception;

import github.taylobe.enumeration.RpcErrorMessageEnum;

/**
 * Rpc异常类
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + " : " + detail);
    }

    public RpcException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}

package github.taylobe.transport;

import github.taylobe.dto.RpcRequest;
import github.taylobe.dto.RpcResponse;
import github.taylobe.enumeration.RpcErrorMessageEnum;
import github.taylobe.enumeration.RpcResponseCode;
import github.taylobe.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * rpc客户端接口
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}

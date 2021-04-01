package github.taylobe.transport;

import github.taylobe.dto.RpcRequest;

/**
 * rpc客户端接口
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}

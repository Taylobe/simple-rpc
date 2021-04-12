package github.taylobe.transport;

import github.taylobe.dto.RpcRequest;

/**
 * rpc客户端接口
 * 实现了 RpcClient 接口的对象需要具有发送 RpcRequest 的能力
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}

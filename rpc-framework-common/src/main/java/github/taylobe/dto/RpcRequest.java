package github.taylobe.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Rpc请求类
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;

    public String interfaceName;
    public String methodName;
    public Object[] parameters;
    public Class<?>[] paramTypes;
}

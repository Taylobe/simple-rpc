package github.taylobe.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Rpc请求类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    public String requestId;
    public String interfaceName;
    public String methodName;
    public Object[] parameters;
    public Class<?>[] paramTypes;
}

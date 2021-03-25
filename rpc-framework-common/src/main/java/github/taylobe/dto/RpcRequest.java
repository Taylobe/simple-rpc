package github.taylobe.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {
    public String interfaceName;
    public String methodName;
    public Object[] parameters;
    public Class<?>[] paramTypes;
}

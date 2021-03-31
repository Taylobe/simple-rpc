package github.taylobe;

import lombok.*;

import java.io.Serializable;

/**
 * hello对象信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String message;
    private String description;
}

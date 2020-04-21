package cn.airesearch.aimarkserver.pojo.requestvo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ZhangXi
 */
@Data
public class IntIdVO {

    @NotNull
    private Integer id;

}

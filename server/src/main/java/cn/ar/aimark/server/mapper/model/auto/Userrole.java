package cn.ar.aimark.server.mapper.model.auto;
import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "cn.ar.aimark.server.mapper.model.auto.Userrole")
@Data
public class Userrole extends BaseModel {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    private Integer userId;

    /**
     * 角色编号
     */
    @ApiModelProperty(value = "角色编号")
    private Integer roleId;
}
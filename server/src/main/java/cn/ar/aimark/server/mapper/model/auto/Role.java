package cn.ar.aimark.server.mapper.model.auto;

import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="cn.ar.aimark.server.mapper.model.auto.Role")
@Data
public class Role extends BaseModel {
    /**
    * 角色编号
    */
    @ApiModelProperty(value="角色编号")
    private Integer roleId;

    /**
    * 角色名称
    */
    @ApiModelProperty(value="角色名称")
    private String roleName;
}
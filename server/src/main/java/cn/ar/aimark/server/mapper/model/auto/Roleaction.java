package cn.ar.aimark.server.mapper.model.auto;

import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "cn.ar.aimark.server.mapper.model.auto.Roleaction")
@Data
public class Roleaction extends BaseModel {
    @ApiModelProperty(value = "null")
    private Integer roleId;

    @ApiModelProperty(value = "null")
    private Integer actionId;
}
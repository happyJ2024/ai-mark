package cn.ar.aimark.server.mapper.model.auto;

import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="cn.ar.aimark.server.mapper.model.auto.Token")
@Data
public class Token extends BaseModel {
    @ApiModelProperty(value="null")
    private String userId;

    @ApiModelProperty(value="null")
    private String accessToken;

    @ApiModelProperty(value="null")
    private String refreshToken;
}
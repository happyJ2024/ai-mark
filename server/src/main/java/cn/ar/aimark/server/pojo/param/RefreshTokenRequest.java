package cn.ar.aimark.server.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 刷新token请求
 * @author yunjian.bian
 */
@ApiModel(description = "刷新token请求")
@Data
public class RefreshTokenRequest {
    @ApiModelProperty(value = "刷新token")
    public String refreshToken;
    @ApiModelProperty(value = "访问token")
    public String accessToken;
}

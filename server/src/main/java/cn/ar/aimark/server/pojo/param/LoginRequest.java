package cn.ar.aimark.server.pojo.param;

import cn.ar.aimark.server.pojo.enums.AppCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登录请求
 *
 * @author yunjian.bian
 */
@ApiModel(description = "登录请求")
public class LoginRequest {
    @ApiModelProperty(value = "用户名")
    public String userName;
    @ApiModelProperty(value = "密码")
    public String password;
    @ApiModelProperty(value = "应用标识")
    public AppCode appCode;

}

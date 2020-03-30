package cn.ar.aimark.server.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册请求
 *
 * @author yunjian.bian
 */
@ApiModel(description = "用户注册请求")
@Data
public class RegisterUserRequest {

    /**
     * 用户登录名, 用于登录
     */
    @ApiModelProperty(value = "用户登录名, 用于登录")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 用户名称, 用于显示
     */
    @ApiModelProperty(value = "用户名称, 用于显示")
    private String name;


    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

}

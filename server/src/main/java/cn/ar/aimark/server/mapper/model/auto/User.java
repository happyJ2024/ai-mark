package cn.ar.aimark.server.mapper.model.auto;

import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "cn.asr.appframework.server.mapper.model.auto.User")
@Data
public class User extends BaseModel {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    private Integer id;

    /**
     * 用户名称, 用于显示
     */
    @ApiModelProperty(value = "用户名称, 用于显示")
    private String name;

    /**
     * 用户登录名, 用于登录
     */
    @ApiModelProperty(value = "用户登录名, 用于登录")
    private String loginName;

    /**
     * 密码, hash后字符串
     */
    @ApiModelProperty(value = "密码, hash后字符串")
    private String passwordHash;

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

    /**
     * 状态, 0=正常, 1=停用
     */
    @ApiModelProperty(value = "状态, 0=正常, 1=停用")
    private Integer status;
}
package cn.ar.aimark.server.mapper.model.auto;
import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@ApiModel(value = "cn.asr.appframework.server.mapper.model.auto.UserLoginHistory")
@Data
public class UserLoginHistory extends BaseModel {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    private Integer userId;

    /**
     * 用户ip地址
     */
    @ApiModelProperty(value = "用户ip地址")
    private String IP;

    /**
     * 上次登录时间
     */
    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginDateTime;

    /**
     * 是否登录成功
     */
    @ApiModelProperty(value = "是否登录成功")
    private Boolean loginSuccess;

    /**
     * 登录失败累计的次数
     */
    @ApiModelProperty(value = "登录失败累计的次数")
    private Integer loginFailedCount;
}
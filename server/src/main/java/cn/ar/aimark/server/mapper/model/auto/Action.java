package cn.ar.aimark.server.mapper.model.auto;

import cn.ar.aimark.server.mapper.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="cn.ar.aimark.server.mapper.model.auto.Action")
@Data
public class Action extends BaseModel {
    /**
    * 操作编号
    */
    @ApiModelProperty(value="操作编号")
    private Integer actionId;

    /**
    * 操作名称
    */
    @ApiModelProperty(value="操作名称")
    private String actionName;

    /**
    * 操作类型, 1=菜单显示, 2=操作权限
    */
    @ApiModelProperty(value="操作类型, 1=菜单显示, 2=操作权限")
    private Integer actionType;

    /**
    * 操作级别, 1=一级菜单, 2=二级菜单
    */
    @ApiModelProperty(value="操作级别, 1=一级菜单, 2=二级菜单")
    private Integer level;

    /**
    * 父级操作编号, 用于多级菜单的控制
    */
    @ApiModelProperty(value="父级操作编号, 用于多级菜单的控制")
    private Integer parentActionId;
}
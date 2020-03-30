package cn.ar.aimark.server.mapper.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础类
 *
 * @author yunjian.bian
 */
@Data
public class BaseModel {
    public BaseModel() {
        deleteFlag = false;
        rowVersion = 1;
    }

    /**
     * 删除标记, 0=正常, 1=删除
     */
    @ApiModelProperty(value = "删除标记, 0=正常, 1=删除")
    private Boolean deleteFlag;

    /**
     * 数据行版本, 每次更新+1
     */
    @ApiModelProperty(value = "数据行版本, 每次更新+1")
    private Integer rowVersion;
}

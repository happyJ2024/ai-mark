package cn.airesearch.aimarkserver.model;

import cn.airesearch.aimarkserver.support.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OCR来源实体类
 *
 * @author ZhangXi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Source extends BaseModel {

    private Integer id;

    private Integer itemId;

    private String originName;

    private String uuidName;

    private String fileType;

    private String urlPath;

}

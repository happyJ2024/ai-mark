package cn.airesearch.aimarkserver.model;

import cn.airesearch.aimarkserver.support.base.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OCR来源实体类
 *
 * @author ZhangXi
 */
@Data
public class Source {

    public static final String COL_ITEM_ID = "item_id";
    public static final String COL_ORIGIN_NAME = "origin_name";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer itemId;

    private String originName;

    private String fileName;

    private String fileType;

    private String urlPath;

    private Boolean isConverted;

}

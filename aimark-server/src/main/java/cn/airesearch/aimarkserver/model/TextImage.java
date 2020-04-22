package cn.airesearch.aimarkserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *
 *
 * @author ZhangXi
 */
@Data
public class TextImage {

    public static final String COL_ID = "id";
    public static final String COL_ITEM_ID = "item_id";
    public static final String COL_SOURCE_ID = "source_id";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer itemId;

    private Integer sourceId;

    private String imageName;

    private Integer pageIndex;

    private String urlPath;

}

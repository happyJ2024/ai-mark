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

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer itemId;

    private Integer sourceId;

    private String imageName;

    private Integer pageIndex;

    private String filePath;

    private String urlPath;

}

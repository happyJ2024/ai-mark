package cn.airesearch.aimarkserver.model;

import cn.airesearch.aimarkserver.modelenum.ItemStatus;
import cn.airesearch.aimarkserver.support.base.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标记项目实体类
 *
 * @author ZhangXi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Item extends BaseModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private ItemStatus status;

}

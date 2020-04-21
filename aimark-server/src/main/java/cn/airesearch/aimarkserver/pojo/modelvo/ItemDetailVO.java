package cn.airesearch.aimarkserver.pojo.modelvo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author ZhangXi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDetailVO extends ItemVO {

    private Map<String, List> detail;

}

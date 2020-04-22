package cn.airesearch.aimarkserver.pojo.modelvo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ZhangXi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDetailVO extends ItemVO {

    private List<SourceImgVO> detail;

}

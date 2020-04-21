package cn.airesearch.aimarkserver.service;

import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;

import java.util.List;

/**
 * @author ZhangXi
 */
public interface ItemService {

    List<ItemVO> getEnabledItems();

    /**
     * 新增项目
     * @param vo {@link ItemVO}
     * @return {@link ItemVO}
     */
    ItemVO addItem(ItemVO vo);


    void deleteItem(Integer id);

}

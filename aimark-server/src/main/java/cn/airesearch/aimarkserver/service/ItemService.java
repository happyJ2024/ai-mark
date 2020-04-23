package cn.airesearch.aimarkserver.service;

import cn.airesearch.aimarkserver.pojo.modelvo.ItemDetailVO;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;

import java.util.List;

/**
 *
 *
 * @author ZhangXi
 */
public interface ItemService {

    /**
     * 获取可用的项目列表
     *
     * @return {@link List<ItemVO>}
     */
    List<ItemVO> getEnabledItems();

    /**
     * 新增项目
     *
     * @param vo {@link ItemVO}
     * @return {@link ItemVO}
     */
    ItemVO addItem(ItemVO vo);

    /**
     * 删除项目
     *
     * @param id ID
     */
    void deleteItem(Integer id);

    /**
     * 更新项目
     *
     * @param vo {@link ItemVO}
     */
    void updateItem(ItemVO vo);
    /**
     * 获取项目详情
     *
     * @param id ITEM ID
     * @return {@link ItemDetailVO}
     */
    ItemDetailVO getItemDetail(Integer id);

    boolean publish(Integer id);
}

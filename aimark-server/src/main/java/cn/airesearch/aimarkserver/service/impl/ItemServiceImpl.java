package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.dao.ItemMapper;
import cn.airesearch.aimarkserver.model.Item;
import cn.airesearch.aimarkserver.modelenum.ItemStatus;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;
import cn.airesearch.aimarkserver.service.ItemService;
import cn.airesearch.aimarkserver.tool.PojoTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ZhangXi
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Override
    public List<ItemVO> getEnabledItems() {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<Item> items = itemMapper.selectList(wrapper);
        List<ItemVO> data = new ArrayList<>();
        for (Item item : items) {
            data.add(new ItemVO(item));
        }
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ItemVO addItem(ItemVO vo) {
        Item item = vo;
        item.setStatus(ItemStatus.UNDONE);
        itemMapper.insert(item);
        PojoTool.copyModelToVo(item, vo);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteItem(Integer id) {
        // 检查是否存在
        Item existOne = itemMapper.selectById(id);
        if (null == existOne) {
            // todo 抛出异常
            return;
        }
        itemMapper.deleteById(id);
    }

}

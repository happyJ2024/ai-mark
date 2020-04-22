package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.dao.ItemMapper;
import cn.airesearch.aimarkserver.dao.SourceMapper;
import cn.airesearch.aimarkserver.dao.TextImageMapper;
import cn.airesearch.aimarkserver.model.Item;
import cn.airesearch.aimarkserver.model.Source;
import cn.airesearch.aimarkserver.model.TextImage;
import cn.airesearch.aimarkserver.modelenum.ItemStatus;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemDetailVO;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;
import cn.airesearch.aimarkserver.pojo.modelvo.SourceImgVO;
import cn.airesearch.aimarkserver.service.ItemService;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.airesearch.aimarkserver.tool.PojoTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ZhangXi
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private SourceMapper sourceMapper;
    @Resource
    private TextImageMapper textImageMapper;

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

    @Override
    public ItemDetailVO getItemDetail(Integer id) {
        //
        Item item = itemMapper.selectById(id);
        if (null == item) {
            //todo
            return null;
        }
        QueryWrapper<Source> sourceWrapper = new QueryWrapper<>();
        sourceWrapper.eq(Source.COL_ITEM_ID, id);
        List<Source> sources = sourceMapper.selectList(sourceWrapper);
        ItemDetailVO data = new ItemDetailVO();
        PojoTool.copyModelToVo(item, data);
        if (null != sources && sources.size() > 0) {
            List<SourceImgVO> details = new ArrayList<>();
            for (Source source : sources) {
                SourceImgVO vo = new SourceImgVO();
                PojoTool.copyModelToVo(source, vo);
                // 获取图片列表
                QueryWrapper<TextImage> imageWrapper = new QueryWrapper<>();
                imageWrapper.eq(TextImage.COL_SOURCE_ID, source.getId());
                imageWrapper.orderByAsc(TextImage.COL_ID);
                List<TextImage> images = textImageMapper.selectList(imageWrapper);
                if (null != images && images.size() > 0) {
                    List<String> urls = new ArrayList<>();
                    for (TextImage image: images) {
                        urls.add(ResourceConst.RESOURCES_URL_PREFIX + IoTool.URL_PATH_SEPARATOR + image.getUrlPath());
                    }
                    vo.setImageUrls(urls);
                }
                details.add(vo);
            }
            data.setDetail(details);
        }
        return data;
    }

}

package cn.airesearch.aimarkserver.dao;

import cn.airesearch.aimarkserver.model.Item;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ZhangXi
 */
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

}

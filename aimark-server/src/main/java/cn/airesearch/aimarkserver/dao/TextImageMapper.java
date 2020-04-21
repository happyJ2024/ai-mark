package cn.airesearch.aimarkserver.dao;

import cn.airesearch.aimarkserver.model.TextImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ZhangXi
 */
@Mapper
public interface TextImageMapper extends BaseMapper<TextImage> {

    /**
     *
     * @param images
     */
    void batchInsertList(List<TextImage> images);


}

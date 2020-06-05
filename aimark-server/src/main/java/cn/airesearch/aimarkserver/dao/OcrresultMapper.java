package cn.airesearch.aimarkserver.dao;

import cn.airesearch.aimarkserver.model.Ocrresult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OcrresultMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(Ocrresult record);

    int insertSelective(Ocrresult record);

    Ocrresult selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(Ocrresult record);

    int updateByPrimaryKey(Ocrresult record);
}
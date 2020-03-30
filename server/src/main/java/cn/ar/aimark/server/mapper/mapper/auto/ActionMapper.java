package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.Action;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActionMapper {
    /**
     * delete by primary key
     * @param actionId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer actionId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(Action record);

    /**
     * select by primary key
     * @param actionId primary key
     * @return object by primary key
     */
    Action selectByPrimaryKey(Integer actionId);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Action record);
}
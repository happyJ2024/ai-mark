package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.Roleaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleactionMapper {
    /**
     * delete by primary key
     *
     * @param roleId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("roleId") Integer roleId, @Param("actionId") Integer actionId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Roleaction record);

    /**
     * delete by primary key
     *
     * @param roleId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer roleId);
}
package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    /**
     * delete by primary key
     * @param roleId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer roleId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(Role record);

    /**
     * select by primary key
     * @param roleId primary key
     * @return object by primary key
     */
    Role selectByPrimaryKey(Integer roleId);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Role record);
}
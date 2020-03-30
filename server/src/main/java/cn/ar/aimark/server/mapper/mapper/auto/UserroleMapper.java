package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.Userrole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserroleMapper {
    /**
     * delete by primary key
     *
     * @param userId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Userrole record);

    /**
     * delete by primary key
     *
     * @param userId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer userId);

    List<Userrole> selectByUserId(Integer userId);

}
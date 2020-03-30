package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.UserLoginHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginHistoryMapper {
    /**
     * delete by primary key
     *
     * @param userId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("IP") String IP);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(UserLoginHistory record);

    /**
     * select by primary key
     *
     * @param userId primary key
     * @return object by primary key
     */
    UserLoginHistory selectByPrimaryKey(@Param("userId") Integer userId, @Param("IP") String IP);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(UserLoginHistory record);
}
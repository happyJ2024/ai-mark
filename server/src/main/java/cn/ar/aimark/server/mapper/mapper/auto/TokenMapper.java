package cn.ar.aimark.server.mapper.mapper.auto;

import cn.ar.aimark.server.mapper.model.auto.Token;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TokenMapper {
    /**
     * delete by primary key
     * @param userId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String userId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(Token record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(Token record);

    /**
     * select by primary key
     * @param userId primary key
     * @return object by primary key
     */
    Token selectByPrimaryKey(String userId);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(Token record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Token record);

    List<Token> selectAll();
}
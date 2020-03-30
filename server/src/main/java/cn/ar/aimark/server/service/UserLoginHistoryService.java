package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.UserLoginHistory;

public interface UserLoginHistoryService {


    int deleteByPrimaryKey(Integer userId, String IP);

    int insert(UserLoginHistory record);

    UserLoginHistory selectByPrimaryKey(Integer userId, String IP);

    int updateByPrimaryKey(UserLoginHistory record);

}



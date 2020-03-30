package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Roleaction;
public interface RoleactionService{


    int deleteByPrimaryKey(Integer roleId,Integer actionId);

    int insert(Roleaction record);

}

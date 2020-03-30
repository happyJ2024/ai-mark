package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Action;
public interface ActionService{


    int deleteByPrimaryKey(Integer actionId);

    int insert(Action record);

    Action selectByPrimaryKey(Integer actionId);

    int updateByPrimaryKey(Action record);

}

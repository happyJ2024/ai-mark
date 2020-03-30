package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Role;
public interface RoleService{


    int deleteByPrimaryKey(Integer roleId);

    int insert(Role record);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKey(Role record);

}

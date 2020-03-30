package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Role;
import cn.ar.aimark.server.mapper.model.auto.Userrole;

import java.util.List;

public interface UserroleService {


    int deleteByPrimaryKey(Integer userId, Integer roleId);

    int insert(Userrole record);

    List<Userrole> selectByUserId(Integer userId);
}


package cn.ar.aimark.server.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.ar.aimark.server.mapper.model.auto.Userrole;
import cn.ar.aimark.server.mapper.mapper.auto.UserroleMapper;
import cn.ar.aimark.server.service.UserroleService;

import java.util.List;

@Service
public class UserroleServiceImpl implements UserroleService {

    @Resource
    private UserroleMapper userroleMapper;

    @Override
    public int deleteByPrimaryKey(Integer userId, Integer roleId) {
        return userroleMapper.deleteByPrimaryKey(userId, roleId);
    }

    @Override
    public int insert(Userrole record) {
        return userroleMapper.insert(record);
    }

    @Override
    public List<Userrole> selectByUserId(Integer userId) {
        return userroleMapper.selectByUserId(userId);
    }

}



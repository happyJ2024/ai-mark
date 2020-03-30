package cn.ar.aimark.server.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.ar.aimark.server.mapper.model.auto.Action;
import cn.ar.aimark.server.mapper.mapper.auto.ActionMapper;
import cn.ar.aimark.server.service.ActionService;
@Service
public class ActionServiceImpl implements ActionService{

    @Resource
    private ActionMapper actionMapper;

    @Override
    public int deleteByPrimaryKey(Integer actionId) {
        return actionMapper.deleteByPrimaryKey(actionId);
    }

    @Override
    public int insert(Action record) {
        return actionMapper.insert(record);
    }

    @Override
    public Action selectByPrimaryKey(Integer actionId) {
        return actionMapper.selectByPrimaryKey(actionId);
    }

    @Override
    public int updateByPrimaryKey(Action record) {
        return actionMapper.updateByPrimaryKey(record);
    }

}

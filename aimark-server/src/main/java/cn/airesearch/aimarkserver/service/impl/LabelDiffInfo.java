package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.pojo.requestvo.DiffAction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LabelDiffInfo {
    String title;
    List<LabelDiff> labelDiffList;

    public void addDiff(String fieldName, String oldValue, String newValue, DiffAction diffAction) {
        labelDiffList.add(new LabelDiff(fieldName, oldValue, newValue, diffAction));
    }

    public LabelDiffInfo() {
        labelDiffList = new ArrayList<>();
    }
}

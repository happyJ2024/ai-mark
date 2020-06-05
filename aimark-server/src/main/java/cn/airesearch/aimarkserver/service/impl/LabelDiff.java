package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.pojo.requestvo.DiffAction;
import lombok.Data;

@Data
public class LabelDiff {
    DiffAction diffAction;
    String oldValue;
    String fieldName;
    String newValue;

    public LabelDiff(String fieldName, String oldValue, String newValue, DiffAction diffAction) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.diffAction = diffAction;
    }
}

package cn.airesearch.aimarkserver.pojo.requestvo;


import lombok.Data;

@Data
public class Label {
    int imageIndex;
    LabelRect rect;
    int labelGroupId;
    String labelValue;
    String labelName;
}

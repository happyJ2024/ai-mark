package cn.airesearch.aimarkserver.pojo.modelvo;

import cn.airesearch.aimarkserver.tool.PDFTextObject;
import lombok.Data;

import java.util.List;

@Data
public class DigitalWaybillInfo {
    int pageNum;
    List<PDFTextObject> wordList;

}

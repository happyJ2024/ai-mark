package cn.airesearch.aimarkserver.support.ocr;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OCRRequest {
    List<ImageInfo> images;
    String waybillJsonPath;

    public OCRRequest() {
        images = new ArrayList<ImageInfo>();
    }
}

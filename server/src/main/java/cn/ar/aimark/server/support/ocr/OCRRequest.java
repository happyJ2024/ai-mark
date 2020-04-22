package cn.ar.aimark.server.support.ocr;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OCRRequest {
    List<ImageInfo> images;

    public OCRRequest() {
        images = new ArrayList<ImageInfo>();
    }
}

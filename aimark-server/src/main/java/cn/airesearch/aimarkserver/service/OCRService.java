package cn.airesearch.aimarkserver.service;


import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.support.ocr.OCRResponse;

import java.util.HashMap;

public interface OCRService {
    BaseResponse<OCRResponse> ocr(Integer projectId);

    void export(String projectId, OCRResponse ocrResponse, HashMap<String, String> idFileMap);
}

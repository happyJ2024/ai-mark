package cn.airesearch.aimarkserver.service;


import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.support.ocr.OCRResponse;

public interface OCRService {
    BaseResponse<OCRResponse> ocr(Integer projectId);
}

package cn.airesearch.aimarkserver.service;


import cn.airesearch.aimarkserver.support.base.BaseResponse;

public interface OCRService {
    BaseResponse<String> ocr(Integer projectId);
}

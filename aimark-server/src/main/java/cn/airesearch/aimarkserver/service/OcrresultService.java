package cn.airesearch.aimarkserver.service;

import cn.airesearch.aimarkserver.model.Ocrresult;
import cn.airesearch.aimarkserver.pojo.requestvo.UpdateOCRResultVO;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.support.ocr.OCRResponse;

import java.util.HashMap;

public interface OcrresultService {


    int deleteByPrimaryKey(Integer itemId);

    int insert(Ocrresult record);

    int insertSelective(Ocrresult record);

    Ocrresult selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(Ocrresult record);

    int updateByPrimaryKey(Ocrresult record);

    BaseResponse updateOCRResult(UpdateOCRResultVO vo);

    void saveOCRResultIntoDb(Integer projectId, OCRResponse ocrResponse, HashMap<String, String> idFileMap);

}









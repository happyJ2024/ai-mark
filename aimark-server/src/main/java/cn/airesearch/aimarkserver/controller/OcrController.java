package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.support.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangXi
 */
@Slf4j
@RestController
public class OcrController {


    @PostMapping(value = "/api/ocrSplit")
    public BaseResponse ocrSplit() {
        //todo ocr分类单据
        return null;
    }

    @PostMapping(value = "/api/ocrExtract")
    public BaseResponse ocrExtract() {
        //todo ocr提取单据
        return null;
    }





}

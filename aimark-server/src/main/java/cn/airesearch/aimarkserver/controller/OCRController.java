package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.pojo.requestvo.IntIdVO;
import cn.airesearch.aimarkserver.service.OCRService;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangXi
 */
@Slf4j
@RestController
public class OCRController {

    private final OCRService OCRService;

    @Autowired
    public OCRController(OCRService OCRService) {
        this.OCRService = OCRService;
    }

    @PostMapping(value = "/api/ocr")
    public BaseResponse<String> ocr(@RequestBody @Validated IntIdVO vo) {

        BaseResponse response = OCRService.ocr(vo.getId());
        return response;
    }


}

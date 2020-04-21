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

    /**
     * 调用OCR识别
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/api/startConvert")
    public BaseResponse startConvert(@RequestParam("id") Integer id) {
        // todo 启动异步任务处理
        BaseResponse response = new BaseResponse();
        response.success();
        return response;
    }




}

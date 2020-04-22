package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.constant.SysErrorCode;
import cn.airesearch.aimarkserver.pojo.requestvo.IntIdVO;
import cn.airesearch.aimarkserver.service.SourceService;
import cn.airesearch.aimarkserver.support.ItemConvert;
import cn.airesearch.aimarkserver.support.ItemConvertManager;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @author ZhangXi
 */
@Tag(name = "/api", description = "源文件接口")
@Validated
@Slf4j
@RestController
public class SourceController {

    private final SourceService sourceService;

    @Autowired
    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }


    @Operation(
            summary = "上传源文件"
    )
    @PostMapping(value = "/api/uploadFile")
    public BaseResponse uploadSource(@RequestParam("file")MultipartFile file, @RequestParam("id") Integer id) {
        // 检查file参数
        String originName = file.getOriginalFilename();
        if (StrUtil.hasBlank(originName)) {
            //todo 抛出异常
        }
        if (file.getSize() == 0) {
            // todo 抛出异常
        }
        try {
            sourceService.saveSourceFile(file, id);
            BaseResponse response = new BaseResponse();
            response.success("源文件上传成功");
            return response;
        } catch (IOException e) {
            // todo 抛出异常
            e.printStackTrace();
            BaseResponse response = new BaseResponse();
            response.fail();
            return response;
        }
    }

    @PostMapping(value = "/api/startConvert")
    public BaseResponse startConvert(@RequestBody @Validated IntIdVO vo) {
        boolean canStart = sourceService.canStartConvert(vo.getId());
        BaseResponse response = new BaseResponse();
        if (canStart) {
            sourceService.asyncConvertPdf(vo.getId());
            response.success("转换启动...");
        } else {
            response.fail(SysErrorCode.NO_SOURCES.getTheDesc(), SysErrorCode.NO_SOURCES);
        }
        return response;
    }

    @GetMapping(value = "/api/getConvertProgress")
    public BaseResponse<ItemConvert> getConvertProgress(@RequestParam("id") @NotNull Integer itemId) {
        ItemConvert convert = ItemConvertManager.get(itemId);
        BaseResponse<ItemConvert> response = new BaseResponse<>();
        if (null == convert) {
            response.fail("无进度");
        } else {
            response.success("进度查询成功", convert);
        }
        return response;
    }

}

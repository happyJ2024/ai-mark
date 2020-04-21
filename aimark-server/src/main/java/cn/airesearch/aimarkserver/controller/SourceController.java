package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.asr.appframework.utility.lang.StringExtUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ZhangXi
 */
@RestController
public class SourceController {

    @GetMapping(value = "/api/test")
    public BaseResponse get() {
        return new BaseResponse();
    }


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
        String filePath = ResourceConst.ITEMS_PATH + IoTool.FILE_PATH_SEPARATOR + "test" +
                IoTool.FILE_PATH_SEPARATOR + StringExtUtils.generatePureUUID() + "." + getFileType(originName);

        Path path = Paths.get(filePath);
        try {
            Files.write(path, file.getBytes());
            // 保存数据


            BaseResponse response = new BaseResponse();
            response.success("图片上传成功");
            return response;
        } catch (IOException e) {
            // todo 抛出异常
            e.printStackTrace();
            BaseResponse response = new BaseResponse();
            response.fail();
            return response;
        }
    }


    private String getFileType(String originName) {
        assert null != originName;
        return originName.substring(originName.lastIndexOf("."));
    }



}

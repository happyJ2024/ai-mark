package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.dao.SourceMapper;
import cn.airesearch.aimarkserver.model.Source;
import cn.airesearch.aimarkserver.service.SourceService;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.asr.appframework.utility.lang.StringExtUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ZhangXi
 */
@Service
public class SourceServiceImpl implements SourceService {

    @Resource
    private SourceMapper sourceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSourceFile(MultipartFile file, Integer itemId) {
        String dirName = ResourceConst.ITEMS_PATH +
                IoTool.FILE_PATH_SEPARATOR + "project" + itemId;
        String fileName = StringExtUtils.generatePureUUID() +
                IoTool.FILE_DOT + IoTool.getFileType(file.getOriginalFilename());
        try {
            // 检查是否存在文件夹
            Path dirPath = Paths.get(dirName);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            String fullName = dirName + IoTool.FILE_PATH_SEPARATOR + fileName;
            Path filePath = Paths.get(fullName);
            Files.write(filePath, file.getBytes());
            // 保存source数据
            Source source = new Source();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

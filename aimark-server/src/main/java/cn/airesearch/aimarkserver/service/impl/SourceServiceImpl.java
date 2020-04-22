package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.AppConst;
import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.dao.SourceMapper;
import cn.airesearch.aimarkserver.dao.TextImageMapper;
import cn.airesearch.aimarkserver.exception.PdfOperationException;
import cn.airesearch.aimarkserver.model.Source;
import cn.airesearch.aimarkserver.model.TextImage;
import cn.airesearch.aimarkserver.service.SourceService;
import cn.airesearch.aimarkserver.support.ItemConvert;
import cn.airesearch.aimarkserver.support.ItemConvertManager;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.airesearch.aimarkserver.tool.PdfTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangXi
 */
@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    @Resource
    private SourceMapper sourceMapper;
    @Resource
    private TextImageMapper textImageMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSourceFile(MultipartFile file, Integer itemId) throws IOException {
        String projectDir = ResourceConst.PROJECT+itemId;
        String dirRootToProject = IoTool.buildFilePath(ResourceConst.ROOT_PATH, projectDir);
        String originName = file.getOriginalFilename();
        try {
            // 检查是否存在文件夹
            Path dirPath = Paths.get(dirRootToProject);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Path fullFilePath = Paths.get(IoTool.buildFilePath(dirRootToProject, originName));
            Files.write(fullFilePath, file.getBytes());
            // 检测是否已有该文件数据
            QueryWrapper<Source> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Source.COL_ITEM_ID, itemId).eq(Source.COL_ORIGIN_NAME, originName);
            Source existOne = sourceMapper.selectOne(queryWrapper);
            if (null == existOne) {
                // 保存source数据
                Source source = new Source();
                source.setItemId(itemId);
                source.setOriginName(originName);
                source.setFileName(IoTool.getFileName(originName));
                source.setFileType(IoTool.getFileType(originName));
                String relativePath = IoTool.buildFilePath(projectDir, originName);
                source.setUrlPath(IoTool.transFileToUrlPath(relativePath));
                sourceMapper.insert(source);
            } else {
                log.warn("已有source数据：{}", existOne.getUrlPath());
            }
        } catch (IOException e) {
            log.error("保存源文件：{} 出错", file.getOriginalFilename(), e);
            throw e;
        }
    }

    @Override
    public boolean canStartConvert(Integer itemId) {
        QueryWrapper<Source> wrapper = new QueryWrapper<>();
        wrapper.eq(Source.COL_ITEM_ID, itemId);
        List<Source> sourceList = sourceMapper.selectList(wrapper);
        if (null == sourceList || sourceList.size() == 0) {
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Async(value = AppConst.EXECUTOR_CONVERT)
    @Override
    public void asyncConvertPdf(Integer itemId) {
        QueryWrapper<Source> wrapper = new QueryWrapper<>();
        wrapper.eq(Source.COL_ITEM_ID, itemId);
        List<Source> sourceList = sourceMapper.selectList(wrapper);
        // 查询对应源文件
        if (null == sourceList || sourceList.size() == 0) {
            // 删除转换状态
            ItemConvertManager.cancel(itemId);
        } else {
            // 注册转换状态
            ItemConvertManager.register(itemId, sourceList.size());
            final String dirRoot = ResourceConst.ROOT_PATH;
            for (int i=0; i<sourceList.size(); i++) {
                Source source = sourceList.get(i);
                // 依次处理源文件
                String fullPdfPath = IoTool.buildFilePath(dirRoot, IoTool.transUrlToFilePath(source.getUrlPath()));
                String dirProjectToName = IoTool.buildFilePath(ResourceConst.PROJECT+itemId, source.getFileName());
                try {
                    String imageDir = IoTool.buildFilePath(dirRoot, dirProjectToName) + File.separator;
                    Path imgDirPath = Paths.get(imageDir);
                    if (!Files.exists(imgDirPath)) {
                        Files.createDirectories(imgDirPath);
                    }
                    // 将pdf转为jpg图片
                    List<String> names = PdfTool.savePdfToImages(new File(fullPdfPath), imageDir, source.getFileName());
                    // 删除并重新保存图片数据
                    QueryWrapper<TextImage> deleteQueryWrapper = new QueryWrapper<>();
                    deleteQueryWrapper.eq(TextImage.COL_ITEM_ID, itemId).eq(TextImage.COL_SOURCE_ID, source.getId());
                    textImageMapper.delete(deleteQueryWrapper);
                    List<TextImage> images = new ArrayList<>();
                    for (int j=0; j<names.size(); j++) {
                        TextImage image = new TextImage();
                        image.setItemId(itemId);
                        image.setSourceId(source.getId());
                        image.setImageName(names.get(j));
                        image.setPageIndex(j+1);
                        String filePath = IoTool.buildFilePath(dirProjectToName, names.get(j));
                        image.setUrlPath(IoTool.transFileToUrlPath(filePath));
                        images.add(image);
                    }
                    textImageMapper.batchInsertList(images);
                    // 修改转换进度
                    ItemConvert convert = ItemConvertManager.get(itemId);
                    convert.setCompleteNumber(i+1);
                    convert.resetPercent();
                    ItemConvertManager.update(itemId, convert);
                    // 修改source状态
                    source.setIsConverted(true);
                    sourceMapper.updateById(source);
                } catch (PdfOperationException | IOException e) {
                    e.printStackTrace();
                }
            }
            // 结束转换状态
            ItemConvertManager.finish(itemId);
        }

    }
}

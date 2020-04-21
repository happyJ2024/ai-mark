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
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.airesearch.aimarkserver.tool.PdfTool;
import cn.asr.appframework.utility.lang.StringExtUtils;
import cn.hutool.core.util.StrUtil;
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
        


        String dirItems$itemXid = ResourceConst.ITEMS +
                IoTool.FILE_PATH_SEPARATOR + "item_" + itemId;
        String dirRootToItemXid = ResourceConst.ROOT_PATH +
                IoTool.FILE_PATH_SEPARATOR + dirItems$itemXid;
        String originName = file.getOriginalFilename();
        String fileType = IoTool.getFileType(originName);
        String fileName = StringExtUtils.generatePureUUID() + IoTool.FILE_DOT + fileType;
        try {
            // 检查是否存在文件夹
            Path dirPath = Paths.get(dirRootToItemXid);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            String fullName = dirRootToItemXid + IoTool.FILE_PATH_SEPARATOR + fileName;
            Path filePath = Paths.get(fullName);
            Files.write(filePath, file.getBytes());
            // 保存source数据
            Source source = new Source();
            source.setItemId(itemId);
            source.setOriginName(originName);
            source.setUuidName(fileName);
            source.setFileType(fileType);
            source.setFilePath(dirItems$itemXid+IoTool.FILE_PATH_SEPARATOR+fileName);
            sourceMapper.insert(source);
        } catch (IOException e) {
            log.error("保存源文件：{} 出错", file.getOriginalFilename(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Async(value = AppConst.EXECUTOR_CONVERT)
    @Override
    public void asyncConvertPdf(Integer itemId) {
        //todo 异步转换pdf为图片
        QueryWrapper<Source> wrapper = new QueryWrapper<>();
        wrapper.eq("item_id", itemId);
        List<Source> sourceList = sourceMapper.selectList(wrapper);
        if (null == sourceList || sourceList.size() == 0) {
            //todo 设置进度为100% 并设置为已完成

        } else {
            // 注册转换状态
            ItemConvertManager.register(itemId, sourceList.size());

            final String dirRoot = ResourceConst.ROOT_PATH;
            for (int i=0; i<sourceList.size(); i++) {
                Source source = sourceList.get(i);
                String uuidName = source.getUuidName();
                String uuid = uuidName.substring(0, uuidName.lastIndexOf(IoTool.FILE_DOT));
                // 依次处理源文件
                String fullPdfPath = dirRoot + IoTool.FILE_PATH_SEPARATOR + source.getFilePath();
                String dirImagesToUuid = ResourceConst.IMAGES +
                        IoTool.FILE_PATH_SEPARATOR + "item_" + itemId +
                        IoTool.FILE_PATH_SEPARATOR + uuid;
                String dirRootToUuid$ = ResourceConst.ROOT_PATH +
                        IoTool.FILE_PATH_SEPARATOR + ResourceConst.RESOURCES +
                        IoTool.FILE_PATH_SEPARATOR + dirImagesToUuid + IoTool.FILE_PATH_SEPARATOR;
                try {
                    // 将pdf转为jpg图片
                    Path imgDirPath = Paths.get(dirRootToUuid$);
                    if (!Files.exists(imgDirPath)) {
                        Files.createDirectories(imgDirPath);
                    }
                    List<String> names = PdfTool.savePdfToImages(new File(fullPdfPath), dirRootToUuid$, uuid);
                    // 批量生成图片数据
                    List<TextImage> images = new ArrayList<>();
                    for (int j=0; j<names.size(); j++) {
                        TextImage image = new TextImage();
                        image.setItemId(itemId);
                        image.setSourceId(source.getId());
                        image.setImageName(names.get(j));
                        image.setPageIndex(j+1);
                        String filePath = dirImagesToUuid + IoTool.FILE_PATH_SEPARATOR + names.get(j);
                        image.setFilePath(filePath);
                        image.setUrlPath(filePath.replace(IoTool.FILE_PATH_SEPARATOR, IoTool.URL_PATH_SEPARATOR));
                        images.add(image);
                    }
                    textImageMapper.batchInsertList(images);
                    // 修改转换状态
                    ItemConvert convert = ItemConvertManager.get(itemId);
                    convert.setCompleteNumber(i+1);
                    convert.resetPercent();
                    ItemConvertManager.update(itemId, convert);
                    // 修改source状态
                    source.setIsConverted(true);
                    sourceMapper.updateById(source);
                } catch (PdfOperationException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // 设置状态为100%
            ItemConvert convert = ItemConvertManager.get(itemId);
            convert.setCompletePercent(100d);
            ItemConvertManager.update(itemId, convert);
        }

    }
}

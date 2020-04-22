package cn.airesearch.aimarkserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ZhangXi
 */
public interface SourceService {

    /**
     *
     * @param file
     * @param itemId
     * @throws IOException
     */
    void saveSourceFile(MultipartFile file, Integer itemId) throws IOException;


    boolean canStartConvert(Integer itemId);

    /**
     * 异步转换pdf图像
     * @param itemId ITEM ID
     */
    void asyncConvertPdf(Integer itemId);

}

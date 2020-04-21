package cn.airesearch.aimarkserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ZhangXi
 */
public interface SourceService {


    void saveSourceFile(MultipartFile file, Integer itemId) throws IOException;


    void asyncConvertPdf(Integer itemId);

}

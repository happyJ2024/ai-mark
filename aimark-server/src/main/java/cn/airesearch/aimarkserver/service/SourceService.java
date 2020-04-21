package cn.airesearch.aimarkserver.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ZhangXi
 */
public interface SourceService {


    void saveSourceFile(MultipartFile file, Integer itemId);


}

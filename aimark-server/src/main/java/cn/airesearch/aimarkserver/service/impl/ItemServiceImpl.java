package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.AppConst;
import cn.airesearch.aimarkserver.constant.ExportConst;
import cn.airesearch.aimarkserver.constant.OcrConst;
import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.dao.ItemMapper;
import cn.airesearch.aimarkserver.dao.SourceMapper;
import cn.airesearch.aimarkserver.dao.TextImageMapper;
import cn.airesearch.aimarkserver.model.Item;
import cn.airesearch.aimarkserver.model.Ocrresult;
import cn.airesearch.aimarkserver.model.Source;
import cn.airesearch.aimarkserver.model.TextImage;
import cn.airesearch.aimarkserver.modelenum.ItemStatus;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemDetailVO;
import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;
import cn.airesearch.aimarkserver.pojo.modelvo.SourceImgVO;
import cn.airesearch.aimarkserver.service.ItemService;
import cn.airesearch.aimarkserver.service.OCRService;
import cn.airesearch.aimarkserver.service.OcrresultService;
import cn.airesearch.aimarkserver.support.ocr.OCRResponse;
import cn.airesearch.aimarkserver.tool.FtpTool;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.airesearch.aimarkserver.tool.PojoTool;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZhangXi
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private OCRService ocrService;
    @Autowired
    private OcrresultService ocrresultService;

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private SourceMapper sourceMapper;
    @Resource
    private TextImageMapper textImageMapper;

    @Override
    public List<ItemVO> getEnabledItems() {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<Item> items = itemMapper.selectList(wrapper);
        List<ItemVO> data = new ArrayList<>();
        for (Item item : items) {
            data.add(new ItemVO(item));
        }
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ItemVO addItem(ItemVO vo) {
        Item item = vo;
        item.setStatus(ItemStatus.UNDONE);
        itemMapper.insert(item);
        PojoTool.copyModelToVo(item, vo);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteItem(Integer id) {
        // 检查是否存在
        Item existOne = itemMapper.selectById(id);
        if (null == existOne) {
            // todo 抛出异常
            return;
        }
        itemMapper.deleteById(id);
    }

    @Override
    public void updateItem(ItemVO vo) {
        itemMapper.updateById(vo);
    }

    @Override
    public ItemDetailVO getItemDetail(Integer id) {
        //
        Item item = itemMapper.selectById(id);
        if (null == item) {
            //todo
            return null;
        }
        QueryWrapper<Source> sourceWrapper = new QueryWrapper<>();
        sourceWrapper.eq(Source.COL_ITEM_ID, id);
        List<Source> sources = sourceMapper.selectList(sourceWrapper);
        ItemDetailVO data = new ItemDetailVO();
        PojoTool.copyModelToVo(item, data);
        if (null != sources && sources.size() > 0) {
            List<SourceImgVO> details = new ArrayList<>();
            for (Source source : sources) {
                SourceImgVO vo = new SourceImgVO();
                PojoTool.copyModelToVo(source, vo);
                // 获取图片列表
                QueryWrapper<TextImage> imageWrapper = new QueryWrapper<>();
                imageWrapper.eq(TextImage.COL_SOURCE_ID, source.getId());
                imageWrapper.orderByAsc(TextImage.COL_ID);
                List<TextImage> images = textImageMapper.selectList(imageWrapper);
                if (null != images && images.size() > 0) {
                    List<String> urls = new ArrayList<>();
                    for (TextImage image : images) {
                        urls.add(ResourceConst.RESOURCES_URL_PREFIX + IoTool.URL_PATH_SEPARATOR + image.getUrlPath());
                    }
                    vo.setImageUrls(urls);
                }
                details.add(vo);
            }
            data.setDetail(details);
        }
        return data;
    }

    @Override
    public boolean publish(Integer projectId) {

        String projectDir = ResourceConst.PROJECT + projectId;
        String dirRootToProject = IoTool.buildFilePath(ResourceConst.ROOT_PATH, projectDir);

        Ocrresult model = ocrresultService.selectByPrimaryKey(projectId);
        String json = model.getUpdateJson();
        if (StringUtils.isEmpty(json)|| json.equals("{}")) {
            json = model.getOriginJson();
        }
        OCRResponse ocrResponse = JSON.parseObject(json, OCRResponse.class);
        HashMap<String, String> idFileMap = JSON.parseObject(model.getIdfilemap(), HashMap.class);
        ocrService.export(String.valueOf(projectId), ocrResponse, idFileMap);

        File zipFile = new File(dirRootToProject + File.separator + projectId + ".zip");
        File exportedFileDir = new File(dirRootToProject + File.separator + ExportConst.EXPORT_DIR_NAME);

        try {
            FtpTool.Client ftpClient = FtpTool.createConnect(AppConst.FTP_HOST_NAME);
            ftpClient.login(AppConst.FTP_USER_NAME, AppConst.FTP_USER_PASSWORD);
            if (ftpClient.operationIsOK() == false) return false;

            String newDirName = projectId + "";
            ftpClient.cd(AppConst.FTP_BASE_DIR);
            ftpClient.mkdir(newDirName).cd(newDirName);
            log.info("当前FTP路径：{}", ftpClient.getCurrentDirPath());
            ftpClient.upload(zipFile.getName(), zipFile.toPath());

            for (File subFile : exportedFileDir.listFiles()) {
                if (subFile.isFile()) {
                    ftpClient.upload(subFile.getName(), subFile.toPath());
                }
            }
            for (File subFile : exportedFileDir.listFiles()) {
                if (subFile.isDirectory()) {
                    ftpClient.mkdir(subFile.getName()).cd(subFile.getName());
                    for (File sub2File : subFile.listFiles()) {
                        if (sub2File.isFile()) {
                            ftpClient.upload(sub2File.getName(), sub2File.toPath());
                        }
                    }
                }
                ftpClient.cd(AppConst.FTP_BASE_DIR).cd(newDirName);
            }

            FtpTool.disConnect(ftpClient);

            ItemDetailVO data = getItemDetail(projectId);
            data.setStatus(ItemStatus.DONE);
            updateItem(data);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

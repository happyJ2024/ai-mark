package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.ExportConst;
import cn.airesearch.aimarkserver.constant.OcrConst;
import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.dao.OcrresultMapper;
import cn.airesearch.aimarkserver.model.Ocrresult;
import cn.airesearch.aimarkserver.pojo.requestvo.DiffAction;
import cn.airesearch.aimarkserver.pojo.requestvo.Label;
import cn.airesearch.aimarkserver.pojo.requestvo.UpdateOCRResultVO;
import cn.airesearch.aimarkserver.service.OcrresultService;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.support.ocr.OCRExporter;
import cn.airesearch.aimarkserver.support.ocr.OCRResponse;
import cn.airesearch.aimarkserver.support.ocr.ai.BaseRectWords;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.InvoiceModel;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.Item;
import cn.airesearch.aimarkserver.support.ocr.ai.waybill.WayBillModel;
import cn.airesearch.aimarkserver.tool.IoTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OcrresultServiceImpl implements OcrresultService {

    @Resource
    private OcrresultMapper ocrresultMapper;

    @Override
    public int deleteByPrimaryKey(Integer itemId) {
        return ocrresultMapper.deleteByPrimaryKey(itemId);
    }

    @Override
    public int insert(Ocrresult record) {
        return ocrresultMapper.insert(record);
    }

    @Override
    public int insertSelective(Ocrresult record) {
        return ocrresultMapper.insertSelective(record);
    }

    @Override
    public Ocrresult selectByPrimaryKey(Integer itemId) {
        return ocrresultMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public int updateByPrimaryKeySelective(Ocrresult record) {
        return ocrresultMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Ocrresult record) {
        return ocrresultMapper.updateByPrimaryKey(record);
    }

    @Override
    public BaseResponse updateOCRResult(UpdateOCRResultVO vo) {
        BaseResponse updateResp = new BaseResponse();
        Integer projectId = vo.getId();

        Ocrresult model = this.ocrresultMapper.selectByPrimaryKey(projectId);
        if (model != null) {

            OCRResponse ocrResponse = JSON.parseObject(model.getOriginJson(), OCRResponse.class);
            List<LabelDiffInfo> labelDiffInfoList = UpdateLabels(ocrResponse, vo.getLabelList());
            if (labelDiffInfoList != null && labelDiffInfoList.size() > 0) {
                String exportDir = IoTool.buildFilePath(ResourceConst.ROOT_PATH, ResourceConst.PROJECT + projectId, ExportConst.EXPORT_DIR_NAME);
                String exportJsonFilePath = IoTool.buildFilePath(exportDir, ExportConst.EXPORT_DIFF_JSON_FILE_NAME);
                OCRExporter.export2Json(exportJsonFilePath, labelDiffInfoList);
            }

            //update
            model.setUpdateJson(formatJson4DB(ocrResponse));
            model.setUpdateDatetime(Date.from(Clock.systemDefaultZone().instant()));
            String difference = formatJson4DB(labelDiffInfoList);
            model.setDifference(difference);
            this.ocrresultMapper.updateByPrimaryKey(model);
            updateResp.success("更新成功");
            updateResp.setData(labelDiffInfoList);
        } else {
            updateResp.success("更新失败，记录不存在");
        }
        return updateResp;
    }

    @Override
    public void saveOCRResultIntoDb(Integer projectId, OCRResponse ocrResponse, HashMap<String, String> idFileMap) {
        Ocrresult model = this.ocrresultMapper.selectByPrimaryKey(projectId);
        if (model == null) {
            //insert
            model = new Ocrresult();
            model.setItemId(projectId);
            model.setOriginJson(formatJson4DB(ocrResponse));
            model.setOcrDatetime(Date.from(Clock.systemDefaultZone().instant()));
            model.setUpdateJson("");
            model.setUpdateDatetime(Date.from(Clock.systemDefaultZone().instant()));
            model.setDifference("");
            model.setIdfilemap(JSON.toJSONString(idFileMap, SerializerFeature.DisableCircularReferenceDetect));

            this.ocrresultMapper.insert(model);
        } else {
            //update
            model.setOriginJson(formatJson4DB(ocrResponse));
            model.setOcrDatetime(Date.from(Clock.systemDefaultZone().instant()));
            model.setUpdateJson("");
            model.setUpdateDatetime(Date.from(Clock.systemDefaultZone().instant()));
            model.setDifference("");
            model.setIdfilemap(JSON.toJSONString(idFileMap, SerializerFeature.DisableCircularReferenceDetect));
            this.ocrresultMapper.updateByPrimaryKey(model);
        }
    }

    private String formatJson4DB(Object obj) {
        String json = JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
        json = json.replace("\\", "\\\\");
        return json;
    }


    private List<LabelDiffInfo> UpdateLabels(OCRResponse ocrResponse, List<Label> labelList) {
        List<LabelDiffInfo> labelDiffInfoList = new ArrayList<>();
        try {

            for (WayBillModel waybill :
                    ocrResponse.waybill) {
                LabelDiffInfo wayBillDiff = updateCommonFields(waybill, labelList, OcrConst.WAYBILL_KEYWORDS_PREFIX);
                if (wayBillDiff.getLabelDiffList().size() > 0) {
                    wayBillDiff.setTitle(OcrConst.WAYBILL_KEYWORDS_PREFIX);
                    labelDiffInfoList.add(wayBillDiff);
                }
            }
            for (InvoiceModel invoice :
                    ocrResponse.invoice) {
                LabelDiffInfo invoiceDiff = updateCommonFields(invoice, labelList, OcrConst.INVOICE_KEYWORDS_PREFIX);
                if (invoiceDiff.getLabelDiffList().size() > 0) {
                    invoiceDiff.setTitle(OcrConst.INVOICE_KEYWORDS_PREFIX + invoice.InvoiceSeq);
                    labelDiffInfoList.add(invoiceDiff);
                }
                for (Item item :
                        invoice.Items) {
                    LabelDiffInfo invoiceItemDiff = updateItemFields(item, labelList, OcrConst.INVOICE_ITEMS_KEYWORDS_PREFIX);
                    if (invoiceItemDiff.getLabelDiffList().size() > 0) {
                        invoiceItemDiff.setTitle(OcrConst.INVOICE_KEYWORDS_PREFIX + invoice.InvoiceSeq + "-" + OcrConst.INVOICE_ITEMS_KEYWORDS_PREFIX + item.ItemSeq);
                        labelDiffInfoList.add(invoiceItemDiff);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return labelDiffInfoList;
    }


    private LabelDiffInfo updateCommonFields(Object model, List<Label> labelList, String keywordsPrefix) throws IllegalAccessException {
        LabelDiffInfo diff = new LabelDiffInfo();
        Field[] fields = model.getClass().getFields();
        for (Field field :
                fields) {
            Object valueObj = field.get(model);
            if ((valueObj instanceof BaseRectWords) == false) {
                continue;
            }
            BaseRectWords value = (BaseRectWords) valueObj;

            String fieldName = field.getName();
            Label findMatchedLabel = null;
            for (Label l :
                    labelList) {
                if (value.pageNum != (l.getImageIndex() + 1)) continue;
                if (l.getLabelName().replace(keywordsPrefix, "").equals(fieldName)) {
                    findMatchedLabel = l;
                    break;
                }
            }

            if (findMatchedLabel == null) {
                //移除该属性值
                diff.addDiff(fieldName, value.words, "", DiffAction.Delete);
                value = new BaseRectWords();

            } else {
                //更新该属性值

                if (valueObj instanceof BaseRectWords) {
                    if (value.words.equals(findMatchedLabel.getLabelValue()) == false) {
                        diff.addDiff(fieldName, value.words, findMatchedLabel.getLabelValue(), DiffAction.Update);
                    }
                    value.words = findMatchedLabel.getLabelValue();
                    value.rect = new ArrayList<>();
                    value.rect.add(findMatchedLabel.getRect().getX());
                    value.rect.add(findMatchedLabel.getRect().getY());
                    value.rect.add(findMatchedLabel.getRect().getX() + findMatchedLabel.getRect().getWidth());
                    value.rect.add(findMatchedLabel.getRect().getY() + findMatchedLabel.getRect().getHeight());
                }
            }
        }
        return diff;
    }

    private LabelDiffInfo updateItemFields(Item item, List<Label> labelList, String keywordsPrefix) throws IllegalAccessException {
        LabelDiffInfo diff = new LabelDiffInfo();
        Field[] fields = item.getClass().getFields();
        for (Field field :
                fields) {
            Object valueObj = field.get(item);
            if ((valueObj instanceof BaseRectWords) == false) {
                continue;
            }
            BaseRectWords value = (BaseRectWords) valueObj;

            String fieldName = field.getName();
            Label findMatchedLabel = null;
            for (Label l :
                    labelList) {
                if (value.pageNum != (l.getImageIndex() + 1)) continue;
                if (item.ItemSeq != l.getLabelGroupId()) continue;

                if (l.getLabelName().replace(keywordsPrefix, "").equals(fieldName)) {
                    findMatchedLabel = l;
                    break;
                }
            }

            if (findMatchedLabel == null) {
                //移除该属性值
                diff.addDiff(fieldName, value.words, "", DiffAction.Delete);
                value = new BaseRectWords();
            } else {
                //更新该属性值
                if (valueObj instanceof BaseRectWords) {
                    if (value.words.equals(findMatchedLabel.getLabelValue()) == false) {
                        diff.addDiff(fieldName, value.words, findMatchedLabel.getLabelValue(), DiffAction.Update);
                    }
                    value.words = findMatchedLabel.getLabelValue();
                    value.rect = new ArrayList<>();
                    value.rect.add(findMatchedLabel.getRect().getX());
                    value.rect.add(findMatchedLabel.getRect().getY());
                    value.rect.add(findMatchedLabel.getRect().getX() + findMatchedLabel.getRect().getWidth());
                    value.rect.add(findMatchedLabel.getRect().getY() + findMatchedLabel.getRect().getHeight());
                }
            }
        }
        return diff;
    }
}









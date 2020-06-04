package cn.airesearch.aimarkserver.support.ocr;

import cn.airesearch.aimarkserver.constant.OcrConst;
import cn.airesearch.aimarkserver.support.ocr.ai.BaseRectWords;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.InvoiceModel;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.Item;
import cn.airesearch.aimarkserver.support.ocr.ai.waybill.WayBillModel;
import cn.airesearch.aimarkserver.tool.HttpUtils;
import cn.airesearch.aimarkserver.tool.JsonUtils;
import cn.asr.appframework.utility.log.Log;
import cn.asr.appframework.utility.log.LoggerWrapper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OCRWrapper {

    private static final boolean MOCK = true;
    private static Log logger = LoggerWrapper.getLogger(String.valueOf(OCRWrapper.class));

    public static OCRResponse callOCRService(OCRRequest ocrRequest) {
        OCRResponse resp = null;

        if (MOCK) {
            File file = new File("/home/byj/Project/园区报关中心OCR测试数据/test/full-waybill-invoice-two.json");
            FileReader reader = null;
            try {
                reader = new FileReader(file);

                char[] buff = new char[(int) file.length()];
                reader.read(buff);
                String jsonStr = new String(buff);
                resp = JsonUtils.jsonToObject(jsonStr, OCRResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if (ocrRequest == null || ocrRequest.getImages() == null || ocrRequest.getImages().size() == 0) {
                throw new NullPointerException("ocrRequest has no images");
            }
            String url = getOCRServiceUrl();

            StopWatch stopWatch = new StopWatch();
            String json = JsonUtils.toJsonString(ocrRequest);
            logger.debug("OCRRequest:" + json);
            stopWatch.start();
            String response = HttpUtils.doPostJson(url, json);

            logger.debug("OCRRespnse:" + response);
            stopWatch.stop();
            long costMilliseconds = stopWatch.getTime(TimeUnit.MILLISECONDS);

            logger.debug("OCR cost milliseconds:" + costMilliseconds);


            if (response != null && response.length() > 0) {
                if (JSON.isValid(response) == false) {
                    logger.error("OCR response is invalid. ");

                }
                resp = JsonUtils.jsonToObject(response, OCRResponse.class);
            }
        }
        FixDataError(resp);
        return resp;
    }

    private static void FixDataError(OCRResponse resp) {

        if (resp != null) {
            for (int i = 0; i < resp.waybill.size(); i++) {
                resp.waybill.get(i).pageNum = resp.waybillPages;
                WayBillModel waybill = resp.waybill.get(i);
                updateField(waybill, resp.waybillPages.get(0));

            }
            for (int i = 0; i < resp.invoice.size(); i++) {
                InvoiceModel invoice = resp.invoice.get(i);
                invoice.InvoiceSeq = i + 1;
                invoice.pageNum = resp.invoicePages.get(i);
                int defaultPageNum = getDefaultPageNum(invoice);
                if (defaultPageNum == 0) {
                    defaultPageNum = invoice.pageNum.get(0);
                }
                updateField(invoice, defaultPageNum);

                for (int j = 0; j < invoice.Items.size(); j++) {
                    Item item = invoice.Items.get(j);
                    item.ItemSeq = j + 1;
                    int defaultPageNum4Item = getDefaultPageNum(item);
                    if (defaultPageNum4Item == 0) {
                        defaultPageNum4Item = defaultPageNum;
                    }
                    updateField(item, defaultPageNum4Item);
                }

            }
        }
    }

    private static int getDefaultPageNum(Object model) {
        List<Integer> pageNumList = new ArrayList<>();
        Field[] fields = model.getClass().getFields();
        for (Field field : fields
        ) {
            Object obj = null;
            try {
                obj = field.get(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj != null && obj instanceof BaseRectWords) {
                BaseRectWords base = (BaseRectWords) obj;
                if (base.pageNum > 0) {
                    pageNumList.add(base.pageNum);
                }
            }
        }
        if (pageNumList.size() > 0) {
            return Collections.min(pageNumList);
        }
        return 0;
    }

    private static void updateField(Object model, int defaultPageNum) {
        Field[] fields = model.getClass().getFields();
        for (Field field : fields
        ) {
            Object obj = null;
            try {
                obj = field.get(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj != null && obj instanceof BaseRectWords) {
                BaseRectWords base = (BaseRectWords) obj;
                if (base.pageNum == 0) {
                    base.pageNum = defaultPageNum;
                }
                if (base.words == null) {
                    base.words = "";
                }
                if (base.rect == null || base.rect.size() == 0) {
                    base.rect = new ArrayList<>();
                    base.rect.add(0);
                    base.rect.add(0);
                    base.rect.add(0);
                    base.rect.add(0);
                }
            }
        }
    }

    private static String getOCRServiceUrl() {
        return OcrConst.OCR_SERVICE_URL;
    }


}

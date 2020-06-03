package cn.airesearch.aimarkserver.support.ocr;

import cn.airesearch.aimarkserver.constant.OcrConst;
import cn.airesearch.aimarkserver.tool.HttpUtils;
import cn.airesearch.aimarkserver.tool.JsonUtils;
import cn.asr.appframework.utility.log.Log;
import cn.asr.appframework.utility.log.LoggerWrapper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileReader;
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
        if (resp != null) {
            for (int i = 0; i < resp.waybill.size(); i++) {
                resp.waybill.get(i).pageNum = resp.waybillPages;
            }
            for (int i = 0; i < resp.invoice.size(); i++) {
                resp.invoice.get(i).pageNum = resp.invoicePages.get(i);
            }
        }
        return resp;
    }

    private static String getOCRServiceUrl() {
        return OcrConst.OCR_SERVICE_URL;
    }


}

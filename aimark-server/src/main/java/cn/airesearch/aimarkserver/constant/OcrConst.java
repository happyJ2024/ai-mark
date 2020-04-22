package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.config.ApplicationContextProvider;

/**
 * @author ZhangXi
 */
public class OcrConst {

    public static final String OCR_SERVICE_URL = ApplicationContextProvider.getBean(AppSetting.class).getOcrServerUrl();


}

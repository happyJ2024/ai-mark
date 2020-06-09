package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.config.ApplicationContextProvider;

/**
 * @author ZhangXi
 */
public class OcrConst {

    public static final String OCR_SERVICE_URL = ApplicationContextProvider.getBean(AppSetting.class).getOcrServerUrl();


    public static final String BACKUP_EXTENDS = "_backup";
    public static final String DOC_NO_KEYWORD = "DOC. NO";


    public static String WAYBILL_KEYWORDS_PREFIX = "运单";
    public static String INVOICE_KEYWORDS_PREFIX = "发票";
    public static String INVOICE_ITEMS_KEYWORDS_PREFIX = "发票条目";


}

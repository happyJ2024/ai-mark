package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.config.ApplicationContextProvider;

/**
 * @author ZhangXi
 */
public class OcrConst {

    public static final String OCR_SERVICE_URL = ApplicationContextProvider.getBean(AppSetting.class).getOcrServerUrl();

    public static final String EXPORT_DIR_NAME = "EXPORTED";

    public  static  final  String BACKUP_EXTENDS="_backup";

    public  static  final  String FILE_NAME_WAYBILL="waybill";
    public  static  final  String FILE_NAME_INVOICE="invoice";
}

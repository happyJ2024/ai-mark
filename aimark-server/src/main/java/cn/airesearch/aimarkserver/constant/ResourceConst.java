package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.config.ApplicationContextProvider;
import cn.airesearch.aimarkserver.tool.IoTool;

/**
 * @author ZhangXi
 */
public final class ResourceConst {

    public static final String RESOURCES = "resources";
    public static final String ITEMS = "items";
    public static final String IMAGES = "images";

    public static final String PROJECT = "project";

    public static final String ROOT_PATH = ApplicationContextProvider.getBean(AppSetting.class).getResourceRoot();
    public static final String RESOURCES_URL_PREFIX = "http://172.16.114.237:30050/resources";





}

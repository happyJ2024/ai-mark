package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.config.ApplicationContextProvider;
import cn.airesearch.aimarkserver.tool.IoTool;

/**
 * @author ZhangXi
 */
public final class ResourceConst {

    public static final String RESOURCES = "resources";
    private static final String ITEMS = "items";
    private static final String IMAGES = "images";

    public static final String ITEMS_PATH = ApplicationContextProvider.getBean(AppSetting.class).getResourceRoot() +
            IoTool.FILE_PATH_SEPARATOR + ITEMS;

    public static final String IMAGES_PATH = ApplicationContextProvider.getBean(AppSetting.class).getResourceRoot() +
            IoTool.FILE_PATH_SEPARATOR + RESOURCES + IoTool.FILE_PATH_SEPARATOR + IMAGES;



}

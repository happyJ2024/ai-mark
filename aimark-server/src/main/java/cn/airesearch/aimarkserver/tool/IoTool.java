package cn.airesearch.aimarkserver.tool;

/**
 * @author ZhangXi
 */
public final class IoTool {

    public static final String URL_PATH_SEPARATOR = "/";
    public static final String FILE_PATH_SEPARATOR = loadFilePathSeparator();
    public static final String FILE_DOT = ".";


    private static String loadFilePathSeparator() {
        String separator = "/";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            separator = "\\";
        }
        return separator;
    }


    public static String getFileType(String originName) {
        return null == originName ? null : originName.substring(originName.lastIndexOf(FILE_DOT));
    }





}

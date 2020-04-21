package cn.airesearch.aimarkserver.tool;

import java.io.File;

/**
 * @author ZhangXi
 */
public final class IoTool {

    private static final String SEPARATOR_LEFT = "/";
    private static final String SEPARATOR_RIGHT = "\\";
    private static final String STRING_EMPTY = "";

    public static final String URL_PATH_SEPARATOR = SEPARATOR_LEFT;
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
        return null == originName ? null : originName.substring(originName.lastIndexOf(FILE_DOT)+1);
    }


    public static String buildFilePath(String... paths) {
        StringBuffer sb = new StringBuffer();
        for (String path : paths) {
            if (path.contains(SEPARATOR_LEFT)) {
                path = path.replace(SEPARATOR_LEFT, STRING_EMPTY);
            }
            if (path.contains(SEPARATOR_RIGHT)) {
                path = path.replace(SEPARATOR_RIGHT, STRING_EMPTY);
            }
            sb.append(File.separator).append(path);
        }
        return sb.toString().substring(1);
    }





}

package cn.ar.aimark.server.pojo.enums;

/**
 * 应用标识
 *
 * @author yunjian.bian
 */
public enum AppCode {

    /**
     *
     */
    WEBAPP("WEBAPP", "前端应用"),

    /**
     *
     */
    SYS("SYS", "管理后台");

    private String enumValue;

    private String enumDescription;

    AppCode(String value, String desc) {
        this.enumValue = value;
        this.enumDescription = desc;
    }

    public String getEnumValue() {
        return this.enumValue;
    }

    public String getEnumDescription() {
        return this.enumDescription;
    }
}

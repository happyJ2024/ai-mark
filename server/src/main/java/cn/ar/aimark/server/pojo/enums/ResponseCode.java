package cn.ar.aimark.server.pojo.enums;

/**
 * 请求响应代码枚举
 * @author Common
 */
public enum ResponseCode {

    /**
     *
     */
    Success(1, "Success"),

    /**
     *
     */
    Failure(0, "Failure");

    private int enumValue;

    private String enumDescription;


    ResponseCode(int value, String desc) {
        this.enumValue = value;
        this.enumDescription = desc;
    }


    public int getEnumValue() {
        return this.enumValue;
    }


    public String getEnumDescription() {
        return this.enumDescription;
    }
}

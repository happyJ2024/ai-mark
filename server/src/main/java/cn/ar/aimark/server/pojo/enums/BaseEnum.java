package cn.ar.aimark.server.pojo.enums;


public class BaseEnum {
    private int enumValue;

    private String enumDescription;


    BaseEnum(int value, String desc) {
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

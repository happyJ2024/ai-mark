package cn.ar.aimark.server.pojo.enums;

/**
 * 用户状态
 *
 * @author yunjian.bian
 */
public enum UserStatus {

    /**
     *
     */
    NORMAL(0, "正常"),

    /**
     *
     */
    FREEZE(1, "停用"),

    /**
     *
     */
    PENDING_VALIDATE(2, "待验证");

    private int enumValue;

    private String enumDescription;


    UserStatus(int value, String desc) {
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

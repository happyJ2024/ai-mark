package cn.ar.aimark.server.pojo.enums;

/**
 * 用户角色类型定义
 *
 * @author yunjian.bian
 */
public enum RoleType {

    /**
     *
     */
    USER(1, "普通用户"),

    /**
     *
     */
    SUPERADMIN(100, "超级管理员");

    private int enumValue;

    private String enumDescription;

    RoleType(int value, String desc) {
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

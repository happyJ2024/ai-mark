package cn.airesearch.aimarkserver.modelenum;

import cn.airesearch.aimarkserver.support.IntGetter;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ZhangXi
 */
public enum ItemStatus implements IntGetter {

    /**
     *
     */
    UNDONE(0, "undone", "项目未完成"), DONE(1, "done", "项目已完成");

    @EnumValue
    private Integer theValue;

    @JsonValue
    private String theName;

    private String theDesc;

    ItemStatus(Integer theValue, String theName, String theDesc) {
        this.theValue = theValue;
        this.theName = theName;
        this.theDesc = theDesc;
    }

    public Integer getTheValue() {
        return theValue;
    }

    public void setTheValue(Integer theValue) {
        this.theValue = theValue;
    }

    public String getTheName() {
        return theName;
    }

    public void setTheName(String theName) {
        this.theName = theName;
    }

    public String getTheDesc() {
        return theDesc;
    }

    public void setTheDesc(String theDesc) {
        this.theDesc = theDesc;
    }

    @Override
    public Integer take() {
        return this.theValue;
    }

    @Override
    public String toString() {
        return this.theName;
    }
}

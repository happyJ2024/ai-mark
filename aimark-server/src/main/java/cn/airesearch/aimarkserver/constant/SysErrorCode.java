package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.support.ErrorCode;

/**
 * @author ZhangXi
 */
public enum SysErrorCode implements ErrorCode {

    /**
     *
     */
    NO_SOURCES(10010, "无源文件数据");

    private Integer theCode;

    private String theDesc;

    SysErrorCode(Integer theCode, String theDesc) {
        this.theCode = theCode;
        this.theDesc = theDesc;
    }

    public Integer getTheCode() {
        return theCode;
    }

    public String getTheDesc() {
        return theDesc;
    }

    @Override
    public Integer take() {
        return this.theCode;
    }
}

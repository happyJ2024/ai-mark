package cn.airesearch.aimarkserver.constant;

import cn.airesearch.aimarkserver.support.StringGetter;

/**
 * API响应状态枚举
 *
 * @author ZhangXi
 */
public enum ResponseStatus implements StringGetter {

    /**
     *
     */
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    UNAUTHORIZED("UNAUTHORIZED"),
    DENIED("DENIED");

    private String enumValue;

    ResponseStatus(String enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public String take() {
        return this.enumValue;
    }
}

package cn.airesearch.aimarkserver.support.base;

import cn.airesearch.aimarkserver.constant.ResponseStatus;
import cn.airesearch.aimarkserver.support.ErrorCode;
import lombok.Data;

/**
 * 基础API响应VO类
 *
 * @author ZhangXi
 */
@Data
public class BaseResponse<T> {

    private static final String DEFAULT_MESSAGE = "";

    private ResponseStatus status;

    private Integer errorCode;

    private T data;

    private String message;


    public void success() {
        this.status = ResponseStatus.SUCCESS;
        this.errorCode = 0;
    }

    public void success(String message) {
        success();
        this.message = message;
    }

    public void success(String message, T data) {
        success(message);
        this.data = data;
    }

    public void fail() {
        this.status = ResponseStatus.FAILURE;
    }

    public void fail(String message) {
        fail();
        this.message = message;
    }

    public void fail(String message, ErrorCode errorCode) {
        fail(message);
        if(null != errorCode) {
            this.errorCode = errorCode.take();
        }
    }

}

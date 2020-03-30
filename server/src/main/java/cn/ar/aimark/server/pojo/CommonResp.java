package cn.ar.aimark.server.pojo;

import cn.ar.aimark.server.pojo.enums.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Common Resp for http response
 *
 * @author yunjian.bian
 */
@ApiModel(description = "通用响应类型")
public class CommonResp<T> {

    @ApiModelProperty(value = "响应代码")
    public ResponseCode respCode;

    @ApiModelProperty(value = "响应数据")
    public T data;

    @ApiModelProperty(value = "响应消息")
    public String message;

    /**
     * 默认构造函数
     */
    public CommonResp() {
        respCode = ResponseCode.Success;
        message = "Success";
    }


    /**
     * 设置失败消息
     *
     * @param s
     */
    public void fail(String s) {
        respCode = ResponseCode.Failure;
        message = s;
    }

    /**
     * 设置失败消息和数据
     *
     * @param s
     * @param d
     */
    public void fail(String s, T d) {
        respCode = ResponseCode.Failure;
        message = s;
        data = d;
    }


    /**
     * 设置成功消息
     *
     * @param s
     */
    public void success(String s) {
        respCode = ResponseCode.Success;
        message = s;
    }


    /**
     * 设置成功消息和数据
     *
     * @param s
     * @param d
     */
    public void success(String s, T d) {
        respCode = ResponseCode.Success;
        message = s;
        data = d;
    }
}

package cn.ar.aimark.server.controller;

import cn.ar.aimark.server.config.context.UserContext;
import cn.asr.appframework.utility.crypto.EncryptUtils;
import cn.asr.appframework.utility.jwt.annotation.AnonymousAnnotation;
import cn.asr.appframework.utility.jwt.annotation.RequireAuthAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author yunjian.bian
 */
@RestController
@RequestMapping(value = "/test")
@Api(tags = "测试接口")
public class TestController {

    @ApiOperation(value = "通过身份验证访问测试信息", response = String.class)
    @RequireAuthAnnotation("访问此接口需要身份验证")
    @RequestMapping(value = {"/getMessage"}, method = RequestMethod.GET)
    public String getMessage() {
        String userId = UserContext.getUserId();
        String userName = UserContext.getUserName();
        return "通过身份验证访问:userId=" + userId + ",userName=" + userName;
    }

    @ApiOperation(value = "通过匿名访问测试信息", response = String.class)
    @AnonymousAnnotation
    @RequestMapping(value = {"/getAnonymousMessage"}, method = RequestMethod.GET)
    public String getAnonymousMessage() {
        return "匿名访问";
    }


    /**
     * 测试AES加密解密
     *
     * @return
     */
    @ApiOperation(value = "测试AES不同平台不一致问题", response = String.class)
    @AnonymousAnnotation
    @RequestMapping(value = {"/aes"}, method = RequestMethod.GET)
    public String testAESEncodeAndDecode() {
        String content = "中国人口最多的城市：北上广排不上榜，这个“黑马”城市称第一";
        String key = "ai-research";
        String encode = EncryptUtils.AESencode(content, key);
        String decode = EncryptUtils.AESdecode(encode, key);
        return "content: " + content + ", key: " + key + " ,encode: " + encode + " ,decode: " + decode;

    }

}

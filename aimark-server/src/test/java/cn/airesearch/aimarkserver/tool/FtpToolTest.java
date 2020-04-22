package cn.airesearch.aimarkserver.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ZhangXi
 */
@Slf4j
public class FtpToolTest {

    @Test
    public void testFTP() throws IOException {
        FtpTool.Client client = FtpTool.createConnect("58.210.237.170", 21);
        client.login("ocr", "ocr0408");
        client.cd("/ocr").mkdir("testvalue").cd("testvalue");
        log.info("当前路径：{}", client.getCurrentDirPath());
        assertTrue(client.operationIsOK(), "操作不成功");
        FtpTool.disConnect(client);
    }



}

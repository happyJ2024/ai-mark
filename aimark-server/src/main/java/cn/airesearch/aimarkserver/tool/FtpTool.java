package cn.airesearch.aimarkserver.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * FTP工具类
 *
 * @author ZhangXi
 */
@Slf4j
public final class FtpTool {

    public static final int DEFAULT_PORT = 21;
    private static final String SEPARATOR = "/";

    /**
     * FTPClient包装
     */
    public static class Client {

        private FTPClient ftpClient;

        private boolean actIsOk = true;

        private String currentDirPath;

        public boolean operationIsOK() {
            return this.actIsOk;
        }

        public boolean isConnected() {
            return ftpClient.isConnected();
        }

        /**
         * 用户登录
         *
         * @param username 用户名
         * @param password 密码
         */
        public Client login(String username, String password) throws IOException {
            this.actIsOk = ftpClient.login(username, password);
            // 判断是否登录失败
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                this.actIsOk = false;
            }
            return this;
        }

        /**
         * 进入目录
         *
         * @param pathName 目录名称，可以是绝对路径或者子目录名称
         */
        public Client cd(String pathName) throws IOException {
            this.actIsOk = ftpClient.changeWorkingDirectory(pathName);
            if (actIsOk) {
                this.currentDirPath = ftpClient.printWorkingDirectory();
            }
            return this;
        }


        public FTPFile[] listDir(String pathName) throws IOException {
            return ftpClient.listDirectories();
        }


        public FTPFile[] listFile(String pathName) throws IOException {
            return ftpClient.listFiles(pathName);
        }

        /**
         * 创建文件夹
         *
         * @param dirName 文件夹名称
         */
        public Client mkdir(String dirName) throws IOException {
            // 判断文件夹是否存在

            this.actIsOk = ftpClient.makeDirectory(dirName);
            return this;
        }

        /**
         * 删除文件夹
         *
         * @param dirPathName 文件夹路径，包括绝对路径或者当前目录下的子文件夹名称
         */
        public Client delDir(String dirPathName) throws IOException {
            this.actIsOk = ftpClient.removeDirectory(dirPathName);
            return this;
        }

        /**
         * 上传文件
         *
         * @param saveName    服务端待保存的文件名称
         * @param inputStream {@link InputStream}
         */
        public Client upload(String saveName, InputStream inputStream) throws IOException {
            this.actIsOk = ftpClient.storeFile(saveName, inputStream);
            return this;
        }

        /**
         * 上传文件
         *
         * @param saveName      服务端待保存的文件名称
         * @param localFilePath {@link Path} 文件对象
         */
        public Client upload(String saveName, Path localFilePath) throws IOException {
            InputStream inputStream = Files.newInputStream(localFilePath, StandardOpenOption.READ);
            return upload(saveName, inputStream);
        }

        /**
         * 删除文件
         *
         * @param filePathName 文件名称，可为绝对路径名称或者当下文件名称
         */
        public Client delFile(String filePathName) throws IOException {
            this.actIsOk = ftpClient.deleteFile(filePathName);
            return this;
        }

        /**
         * 下载文件
         *
         * @param remotePathName 远程文件路径
         * @param outputStream   {@link OutputStream}
         */
        public Client download(String remotePathName, OutputStream outputStream) throws IOException {
            // 检查文件是否存在
            InputStream is = ftpClient.retrieveFileStream(remotePathName);
            if (null == is || ftpClient.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
                // 文件不存在
                this.actIsOk = false;
            } else {
                is.close();
                // 这里需要清除命令
                ftpClient.completePendingCommand();
                this.actIsOk = ftpClient.retrieveFile(remotePathName, outputStream);
            }
            return this;
        }

        /**
         * 获取当前目录
         *
         * @return 当前目录绝对路径
         */
        public String getCurrentDirPath() {
            return currentDirPath;
        }

        public FTPClient getFtpClient() {
            return ftpClient;
        }

        public void setFtpClient(FTPClient ftpClient) {
            this.ftpClient = ftpClient;
        }
    }

    /**
     * 创建并连接客户端
     *
     * @param hostName 域名或者IP地址
     * @return {@link Client}
     */
    public static Client createConnect(String hostName) throws IOException {
        return createConnect(hostName, DEFAULT_PORT);
    }


    /**
     * 创建并连接客户端
     *
     * @param hostName 域名或者IP地址
     * @param port     端口
     * @return {@link Client}
     */
    public static Client createConnect(String hostName, int port) throws IOException {
        FTPClient client = new FTPClient();
        client.connect(hostName, port);
        // 设置本地被动模式，由服务端开放端口
        client.enterLocalPassiveMode();
        // 二进制文件格式
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        client.setKeepAlive(true);
        client.setBufferSize(1024);
        client.setControlEncoding("utf-8");
        Client toolClient = new Client();
        toolClient.setFtpClient(client);
        return toolClient;
    }

    /**
     * 退出FTP连接
     *
     * @param toolClient {@link Client}
     */
    public static void disConnect(Client toolClient) throws IOException {
        FTPClient client = toolClient.getFtpClient();
        if (null != client) {
            client.logout();
            client.disconnect();
        }
    }

}

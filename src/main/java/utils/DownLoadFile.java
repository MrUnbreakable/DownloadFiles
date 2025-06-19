package utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhangzhm
 * @date 2025/6/19 13:47
 */
public class DownLoadFile {
    public static void downloadFile(String downloadUrl, String path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(downloadUrl);
            //这里没有使用 封装后的ResponseEntity 就是也是因为这里不适合一次性的拿到结果，放不下content,会造成内存溢出
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();

            //使用bufferedInputStream 缓存流的方式来获取下载文件，不然大文件会出现内存溢出的情况
            inputStream = new BufferedInputStream(connection.getInputStream());
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            outputStream = new FileOutputStream(file);
            //这里也很关键每次读取的大小为5M 不一次性读取完
            byte[] buffer = new byte[1024 * 1024 * 5];// 5MB
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void main(String[] args) {
        String downloadUrl="https://region-anhui-resource.xuexi.cn/audio/1006/p/788b22a320df4f83f68fd37a393e0f2d-7746312e2fb44b5784594fed8e4f490d-1.mp3";
        String path = "D:\\Projects\\downloadFiles\\《习近平关于加强党的作风建设论述摘编》读1（p3-p4）.mp3";
        downloadFile(downloadUrl,path);
    }
}

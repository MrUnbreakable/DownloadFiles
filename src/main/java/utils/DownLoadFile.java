package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author zhangzhm
 * @date 2025/6/19 13:47
 */
public class DownLoadFile {
    public static void downloadFile(String downloadUrl, String path){

        URL url;
        try {
            url = new URL(downloadUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //这里没有使用 封装后的ResponseEntity 就是也是因为这里不适合一次性的拿到结果，放不下content,会造成内存溢出
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("文件删除成功");
            } else {
                System.out.println("文件删除失败");
            }
        }
        //使用bufferedInputStream 缓存流的方式来获取下载文件，不然大文件会出现内存溢出的情况
        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
             OutputStream outputStream = Files.newOutputStream(file.toPath())){
            //这里也很关键每次读取的大小为5M 不一次性读取完
            byte[] buffer = new byte[1024 * 1024 * 5];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            connection.disconnect();
        }catch (Exception e){
            log.print("execute failed:" + e);
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String downloadUrl="https://region-anhui-resource.xuexi.cn/audio/1006/p/788b22a320df4f83f68fd37a393e0f2d-7746312e2fb44b5784594fed8e4f490d-1.mp3";
        String path = "D:\\Projects\\downloadFiles\\《习近平关于加强党的作风建设论述摘编》读1（p3-p4）.mp3";
        downloadFile(downloadUrl,path);
    }
}

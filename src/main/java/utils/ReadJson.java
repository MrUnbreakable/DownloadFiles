package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;
import static utils.DownLoadFile.downloadFile;

/**
 * @author zhangzhm
 * @date 2025/6/19 11:19
 */
public class ReadJson {
    public static void readJson (String filePath) {
        if (StringUtils.isEmpty(filePath) || StringUtils.isBlank(filePath)) {
            filePath = "D:\\Projects\\downloadFiles\\";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 从文件路径读取JSON
            JsonNode rootNode = mapper.readTree(new File(filePath+"test.txt"));

            // 访问具体字段
            JsonNode subNode = rootNode.get("sub_items");
            for(JsonNode node : subNode) {
                String title = node.get("title").asText();
                String url = node.get("audios").get(0).get("audio_storage_info").get(0).get("url").asText();
                System.out.println(title + url);
                downloadFile(url,filePath + title + ".mp3");
                System.out.println(title + "下载完成");
            }
        } catch (IOException e) {
            log.print("execute failed:" + e);
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readJson(null);
    }
}

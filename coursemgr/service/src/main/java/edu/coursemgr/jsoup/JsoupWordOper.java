package edu.coursemgr.jsoup;

import edu.coursemgr.utils.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/9/10 0010 下午 10:04
 */
public class JsoupWordOper {

    public void html2Word(String html, String wordTmpDir, String newFileName){
        MSOfficeGeneratorUtils officeUtils = new MSOfficeGeneratorUtils(false); // 将生成过程设置为不可见
        int imgIndex = 1;
        Map<String, String> imgMap = new HashMap<String, String>(); //存放图片标识符及物理路径  {"image_1","D:\img.png"};
        String imgsDir = CommonUtils.combinePath(wordTmpDir, "imgs");
        try {
            Document document = Jsoup.parse(html);
            Elements elements = document.select("img");

            for (Element img : elements){
                img.after("<p>${image_" + imgIndex + "}</p>"); // 为img添加同级p标签，内容为<p>${image_imgIndexNumber}</p>
                String src = img.attr("src");
                // 下载图片到本地
                download(src,"image_"+imgIndex, imgsDir);
                // 保存图片标识符及物理路径
                imgMap.put("${image_" + imgIndex++ + "}", src);
                // 删除Img标签
                img.remove();
            }
            // 将html代码写到html文件中
            String htmlFilePath = CommonUtils.combinePath(wordTmpDir, "temp.html");
            FileWriter fw = new FileWriter(htmlFilePath);
            fw.write(document.html(), 0, document.html().length());// 写入文件
            fw.flush();
            fw.close();

            // templates.doc为模板，这里模板不动，复制了一个副本 用于写入数据
            String tempFilePath = this.getClass().getClassLoader().getResource("templates/template.doc").getPath();
            FileUtils.copyFile(new File(tempFilePath),new File(newFileName));
            // html文件转为word
            officeUtils.html2Word(htmlFilePath,newFileName);

            // 替换标识符为图片
            for (Map.Entry<String, String> entry : imgMap.entrySet()){
                officeUtils.replaceText2Image(entry.getKey(), entry.getValue());
            }
            officeUtils.saveAs(newFileName);    // 保存
            officeUtils.close(); // 关闭Office Word创建的文档
            officeUtils.quit(); // 退出Office Word程序

            // 删除本地图片
            CommonUtils.deleteDir(imgsDir);
            // 删除html文档
            CommonUtils.deleteFile(htmlFilePath);

            imgMap.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载图片
     * @param urlString  路径
     * @param filename   保存的文件名
     * @param savePath   保存路径
     */
    public void download(String urlString, String filename,String savePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(savePath);
            if(!sf.exists()){
                sf.mkdirs();
            }
            os = new FileOutputStream(sf.getPath() + "\\" + filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

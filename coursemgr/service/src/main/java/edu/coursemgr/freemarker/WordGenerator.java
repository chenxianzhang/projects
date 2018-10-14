package edu.coursemgr.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/10/14 0014 上午 10:50
 */
public class WordGenerator {

    private static Configuration configuration = null;
    private static Map<String, Template> allTemplates = null;

    static {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(WordGenerator.class, "/cn/comm/ftl");
        allTemplates = new HashMap<>(); // Java 7 钻石语法
        try {
            allTemplates.put("template", configuration.getTemplate("wordTemplate.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WordGenerator() {
        throw new AssertionError();
    }

    public static File createDoc(Map<?, ?> dataMap, String type) {
        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(name);
        Template t = allTemplates.get(type);
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.flush();
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }

    //将图片转换成BASE64字符串
    public static String getImageString(InputStream in) throws IOException {
        //InputStream in = null;
        byte[] data = null;
        try {
            // in = new FileInputStream(filename);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null)
                in.close();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }

}

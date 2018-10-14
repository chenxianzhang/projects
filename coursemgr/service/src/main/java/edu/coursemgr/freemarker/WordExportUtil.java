package edu.coursemgr.freemarker;

import edu.coursemgr.utils.CommonUtils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/10/14 0014 上午 9:17
 */
public class WordExportUtil {

    public static String WORD_2007 = "WORD_2007";
    public static String WORD_2003 = "WORD_2003";

    /**
     * 设置下载文件中文件的名称
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");
            return URLEncoder.encode(filename, "UTF-8");
        } catch (Exception ex) {
            return filename;
        }
    }

    /**
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param version  Word_2003/Word_2007
     * @param docFileName 生成的doc临时文件名
     * @param templateFile  freemark模板文件名
     * @param beanParams  入参数据:  Map<String, Object>类型
     */
    public static void writeResponse(HttpServletRequest request, HttpServletResponse response, String version, String docFileName, String templateFile, Map<String, Object> beanParams) {
        writeResponse(request, response, version, "temp", docFileName, "template", templateFile, beanParams);
    }


    /**
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param version  Word_2003/Word_2007
     * @param docTempDir  生成的doc临时文件目录
     * @param docFileName 生成的doc临时文件名
     * @param templateDir 存放freemark模板的目录
     * @param templateFile freemark模板文件名
     * @param beanParams   入参数据:  Map<String, Object>类型
     */
    public static void writeResponse( HttpServletRequest request, HttpServletResponse response, String version,
                                      String docTempDir, String docFileName, String templateDir, String templateFile,
                                      Map<String, Object> beanParams) {
        Configuration config = new Configuration();
        ServletContext sc = request.getSession().getServletContext();
        InputStream is = null;
        File previewFile = null;
        try {
            config.setDirectoryForTemplateLoading(new File(sc.getRealPath("/"+templateDir)));
            config.setObjectWrapper(new DefaultObjectWrapper());
            Template template = config.getTemplate(templateFile, "UTF-8");
            if (WORD_2007.equals(version)) {
                docFileName = encodeFilename(docFileName + ".docx", request);
            } else {
                docFileName = encodeFilename(docFileName + ".doc", request);
            }
            String docName = request.getSession().getServletContext().getRealPath("/"+docTempDir)+docFileName;
            FileOutputStream fos = new FileOutputStream(docName);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            template.process(beanParams, out);
            out.flush();
            out.close();


            previewFile = new File(docName);
            is = new FileInputStream(previewFile);
            response.reset();
            if (WORD_2007.equals(version)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8");
            }else{
                response.setContentType("application/vnd.ms-word;charset=UTF-8");
            }
            response.addHeader("Content-Disposition", "attachment;filename="+docFileName);
            byte[] b = new byte[1024];
            int len;
            while ((len=is.read(b)) >0) {
                response.getOutputStream().write(b,0,len);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(previewFile!=null){
                previewFile.delete();
            }
        }
    }


    public static void generatorWord(HttpServletRequest request, String version, String docFileName, String templateFile,
                                     String outputDir, Map<String, Object> beanParams) {
        Configuration config = new Configuration();
        ServletContext sc = request.getSession().getServletContext();
        InputStream is = null;
        try {
            String tempFilePath = WordExportUtil.class.getClassLoader().getResource("templates").getPath();
//            File tempFile = new File(tempFilePath);
            config.setDefaultEncoding("UTF-8");
            config.setDirectoryForTemplateLoading(new File(tempFilePath));
            config.setObjectWrapper(new DefaultObjectWrapper());
            Template template = config.getTemplate(templateFile, "UTF-8");
            if (WORD_2007.equals(version)) {
//                docFileName = encodeFilename(docFileName + ".docx", request);
                docFileName += ".docx";
            } else {
//                docFileName = encodeFilename(docFileName + ".doc", request);
                docFileName += ".doc";
            }
            String docName = outputDir + "/" + docFileName;
            FileOutputStream fos = new FileOutputStream(docName);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            template.process(beanParams, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

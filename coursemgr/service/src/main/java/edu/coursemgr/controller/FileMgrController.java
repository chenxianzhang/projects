package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.service.interfaces.FileMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by chenxianzhang on 2018/9/2 0002 下午 11:12
 */
@Controller
@RequestMapping("/fileMgr")
public class FileMgrController extends BaseController {


    @Autowired
    private FileMgrService fileMgrService;

    /**
     * data: {
     *    trunkDir:
     *    subDir:
     * }
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadOneFile")
    @ResponseBody
    public Object uploadOneFile(HttpServletRequest request)
            throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator iterator = multipartRequest.getFileNames();
        String trunkDir = multipartRequest.getParameter("trunkDir");
        String subDir = multipartRequest.getParameter("subDir");
        if (CommonUtils.isEmpty(trunkDir)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        if (iterator.hasNext()) {
            String fileKey = iterator.next().toString();
            MultipartFile file = multipartRequest.getFile(fileKey);
            String path = fileMgrService.saveFile(file, request.getRealPath("/"),
                    trunkDir, subDir);
            return path;
        }
        return "";
    }

    @RequestMapping("/downloadStuTemplate")
    @ResponseBody
    public void downloadStuTemplate(HttpServletResponse response) throws Exception {

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode("学生导入模板.xlsx", "UTF-8"))));
        OutputStream out = response.getOutputStream();

        InputStream template = this.getClass().getResourceAsStream("/templates/学生导入模板.xlsx");

        if (template != null) {
            //5.创建数据缓冲区
            byte[] buffer = new byte[1024];
            int len = 0;
            //7.将InputStream流写入到buffer缓冲区
            while ((len = template.read(buffer)) > 0) {
                //template.read(byte[] b)最多读入b.length个字节 在碰到流的结尾时 返回-1
                //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer,0,len);
            }
            template.close();
        }
        out.flush();
        out.close();
    }

}

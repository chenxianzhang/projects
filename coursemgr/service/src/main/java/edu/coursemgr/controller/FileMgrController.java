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

        fileMgrService.downloadTemplate(response, "/templates/学生导入模板.xlsx",
                "学生导入模板.xlsx");
    }

    @RequestMapping("/downloadTeaTemplate")
    @ResponseBody
    public void downloadTeaTemplate(HttpServletResponse response) throws Exception {
        fileMgrService.downloadTemplate(response, "/templates/教师导入模板.xlsx",
                "教师导入模板.xlsx");
    }

}

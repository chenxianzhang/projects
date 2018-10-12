package edu.coursemgr.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by chenxianzhang on 2018/9/2 0002 下午 11:38
 */
public interface FileMgrService {

    /**
     * 保存上传文件，并返回文件路径
     * @param file
     * @param trunkDir
     * @param subDir
     * @return
     */
    String saveFile(MultipartFile file, String root, String trunkDir,
                    String subDir) throws Exception;

    /**
     * 下载模板
     * @param response
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    void downloadTemplate(HttpServletResponse response, String filePath,
                          String fileName) throws Exception;
}

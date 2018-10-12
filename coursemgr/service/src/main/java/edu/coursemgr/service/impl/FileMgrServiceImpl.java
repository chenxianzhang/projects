package edu.coursemgr.service.impl;

import edu.coursemgr.service.interfaces.FileMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by chenxianzhang on 2018/9/2 0002 下午 11:40
 */
@Service
@Transactional
public class FileMgrServiceImpl implements FileMgrService {
    @Override
    public String saveFile(MultipartFile file, String root, String trunkDir, String subDir)
            throws Exception {
        String dir = CommonUtils.combineDir(trunkDir, subDir);


        String dirAll = CommonUtils.combinePath(root, dir);
        CommonUtils.createDir(dirAll);
        if (file.getOriginalFilename().isEmpty()) {
            return "";
        }
        String originfileName = file.getOriginalFilename();
        String[] fileNames = originfileName.split("\\.");
        String extension = fileNames[fileNames.length - 1];
        String fileName = String.format("%s.%s", CommonUtils.uuid(), extension);

        String filePath = CommonUtils.combinePath(dirAll, fileName);
        File f = new File(filePath);
        file.transferTo(f);
        return CommonUtils.combinePath(dir, fileName);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response, String filePath,
                                 String fileName) throws Exception {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        OutputStream out = response.getOutputStream();

        InputStream template = this.getClass().getResourceAsStream(filePath);

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

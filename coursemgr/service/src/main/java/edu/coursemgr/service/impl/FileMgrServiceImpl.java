package edu.coursemgr.service.impl;

import edu.coursemgr.service.interfaces.FileMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.OutputStream;

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
        return CommonUtils.combinePath(dir, file.getOriginalFilename());
    }

}

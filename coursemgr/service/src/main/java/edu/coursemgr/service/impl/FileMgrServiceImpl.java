package edu.coursemgr.service.impl;

import edu.coursemgr.common.Constant;
import edu.coursemgr.service.interfaces.FileMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;

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
        String filePath = CommonUtils.combinePath(dirAll, file.getOriginalFilename());
        File f = new File(filePath);
        file.transferTo(f);
        return CommonUtils.combinePath(dir, file.getOriginalFilename());
    }
}

package edu.coursemgr.utils;

import edu.coursemgr.common.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by chenxianzhang on 2018/8/18 0018 上午 12:18
 */
public final class CommonUtils {

    public static boolean isEmpty(String inputStr) {
        boolean isEmpty = inputStr == null || inputStr.isEmpty()
                || inputStr.equals(Constant.Common.UNDEFINED);
        return isEmpty;
    }

    /**
     * upload/...
     * @param trunkDir
     * @param subDir
     * @return
     */
    public static String combineDir(String trunkDir, String subDir) {
        if (subDir.isEmpty()) {
            Path path = Paths.get(Constant.Common.UPLOAD_ROOT_DIR,
                    trunkDir);
            return path.toString();
        }
        Path path = Paths.get(Constant.Common.UPLOAD_ROOT_DIR,
                trunkDir, subDir);
        return path.toString();
    }

    public static String combinePath(String prev, String... next) {
        Path path = Paths.get(prev, next);
        return path.toString();
    }

    public static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            } else {
                file.mkdir();
            }
        }
    }

    public static void deleteDir(String dir) throws IOException {
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            return;
        }
        File[] listFiles = fileDir.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                deleteDir(file.getPath());
                continue;
            }
            file.delete();
        }
        fileDir.delete();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        file.delete();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String join(Collection collection, String separetor) {
        String result = "";
        for (Object obj : collection) {
            if (result.isEmpty()) {
                result = obj.toString();
                continue;
            }

            result = String.format(",%s", obj.toString());
        }
        return result;
    }

    public static String imgToBase64(String imgPath) {
        if (isEmpty(imgPath)) {
            return "";
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            return "";
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            return "";
        }
        if (data == null) {
            return "";
        }
        Base64.Encoder encoder = Base64.getEncoder();
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
        return encoder.encodeToString(data);
    }
}

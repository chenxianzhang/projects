package edu.coursemgr.utils;

import java.util.ResourceBundle;

/**
 * 全局配置参数工具类
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:56
 */
public final class ResourceUtil {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("global");

    /**
     * 获取随机码的长度
     *
     * @return 随机码的长度
     */
    public static String getRandCodeLength() {
        return bundle.getString("randCodeLength");
    }

    /**
     * 获取随机码的类型
     *
     * @return 随机码的类型
     */
    public static String getRandCodeType() {
        return bundle.getString("randCodeType");
    }
}

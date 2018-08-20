package edu.coursemgr.utils;

import edu.coursemgr.common.Constant;

/**
 * Created by chenxianzhang on 2018/8/18 0018 上午 12:18
 */
public final class CommonUtils {

    public static boolean isEmpty(String inputStr) {
        boolean isEmpty = inputStr == null || inputStr.isEmpty()
                || inputStr.equals(Constant.Common.UNDEFINED);
        return isEmpty;
    }
}

package edu.coursemgr.common;

/**
 * 常量类，使用接口实现
 * create by chenxianzhang
 */
public interface Constant {



    /**
     * 公共常量
     */
    final class Common {

        public static final Object lock = new Object();

        public static final String SEPARATE_COMMA = ",";

        public static final String UNDEFINED = "undefined";

        public static final int PASSWORD_MIN_LENGTH = 6;

        public static final String USER_DIR = System.getProperty("user.dir");

        public static final String UPLOAD_ROOT_DIR = "upload";

        public static final String DOWNLOAD_TEMP_DIR = "download";

        public static final String SUCCESS = "success";
    }

    /**
     * session keys
     */
    final class SessionKey {
        public static final String SESSION_KEY_OF_RAND_CODE = "randCode";
    }

    /**
     * verification code constant
     */
    final class VerificationCode {
        /**
         * 定义图形大小(宽)
         */
        public static final int WIDTH = 105;

        /**
         * 定义图形大小(高)
         */
        public static final int HEIGHT = 45;

        /**
         * 干扰线的长度=1.414*lineWidth
         */
        public static final int LINE_WIDTH = 1;

        /**
         * 干扰线的数量
         */
        public static final int INTERFERING_LINE_COUNT = 200;
    }

    final class ExceptionMessage {

        public static final String LOGIN_TIMEOUT = "用户登录超时，请重新登录";

        public static final String PARAM_EXCEPTION = "参数异常";

        public static final String DATA_SAVE_EXCEPTION = "数据存储异常";

        public static final String DATA_QUERY_EXCEPTION = "数据查询异常";

        public static final String PARAM_EMPTY = "参数为空";

        public static final String EXCEL_EMPTY = "空Excel文件";

        public static final String TASK_DATE_EMPTY = "任务开始或截止日期不能为空";

        public static final String TASK_DATE_EXCEPTION = "任务截止日期不能小于开始日期";

    }
}

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
        public static final String SEPARATE_COMMA = ",";
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
}

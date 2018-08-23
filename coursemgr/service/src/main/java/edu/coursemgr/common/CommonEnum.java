package edu.coursemgr.common;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 1:38
 */
public class CommonEnum {

    public enum StudentTaskStatus {
        FINISHED("FINISHED", "完成"),
        TO_REVIEW("TO_REVIEW", "待完成"),
        UNCOMMITTED("UNCOMMITTED", "未提交");

        private String value;
        private String name;

        StudentTaskStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
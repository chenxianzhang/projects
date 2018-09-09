package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/9/9 0009 上午 10:14
 */
public class PageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalCount;

    private Object pageData;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getPageData() {
        return pageData;
    }

    public void setPageData(Object pageData) {
        this.pageData = pageData;
    }
}

package edu.coursemgr.service.interfaces;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 12:47
 */
public interface GroupMgrService {

    /**
     * 自由分组
     * @param courseId
     * @param memberCnt
     * @return
     */
    boolean freeGroup(String courseId, String memberCnt);

}

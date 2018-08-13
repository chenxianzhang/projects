package edu.coursemgr.common.interfaces;

/**
 * Created by chenxianzhang on 2018/8/9 0009 下午 9:23
 */
public interface Function<R, T> {

    R call(T param);
}

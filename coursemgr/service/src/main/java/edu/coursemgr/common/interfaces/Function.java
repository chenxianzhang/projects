package edu.coursemgr.common.interfaces;

/**
 * Created by chenxianzhang on 2018/8/9 0009 下午 9:23
 */
public interface Function<T, R> {

    R call(T param);
}

package edu.coursemgr.utils;

import edu.coursemgr.common.interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/13 0013 下午 10:08
 */
public final class CollectionUtils {

    /**
     * ArrayList 不同类型之间的转换
     * @param origin
     * @param func
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> arrayListCast(List<T> origin, Function<R, T> func) {
        if (origin == null || func == null) {
            return null;
        }
        List<R> rList = new ArrayList<>(origin.size());
        origin.forEach(item -> {
            R tResult = func.call(item);
            if (tResult != null) {
                rList.add(func.call(item));
            }
        });
        return rList;
    }

    /**
     * ArrayList 不同类型之间的转换  带过滤功能
     * @param origin
     * @param filter
     * @param func
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> arrayListCast(List<T> origin, Function<Boolean, T> filter,
                                               Function<R, T> func) {
        boolean illegal = origin == null || func == null || filter == null;
        if (illegal) {
            return null;
        }
        List<R> rList = new ArrayList<>(origin.size());
        origin.forEach(item -> {
            if (filter.call(item)) {
                R tResult = func.call(item);
                if (tResult != null) {
                    rList.add(func.call(item));
                }
            }
        });
        return rList;
    }

}

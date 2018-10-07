package edu.coursemgr.utils;

import edu.coursemgr.common.interfaces.Function;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by chenxianzhang on 2018/8/13 0013 下午 10:08
 */
public final class CollectionUtils {

    /**
     * ArrayList 不同类型之间的转换 中间转换返回单个对象
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
                rList.add(tResult);
            }
        });
        return rList;
    }

    /**
     * ArrayList 不同类型之间的转换 中间转换返回集合
     * @param origin
     * @param func
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> arrayListCasts(List<T> origin, Function<List<R>, T> func) {
        if (origin == null || func == null) {
            return null;
        }
        List<R> rList = new ArrayList<>(origin.size());
        origin.forEach(item -> {
            List<R> tResult = func.call(item);
            if (tResult != null && tResult.size() > 0) {
                rList.addAll(tResult);
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
                    rList.add(tResult);
                }
            }
        });
        return rList;
    }

    /**
     * 通过反射获取对象中的属性与值得键值对
     * @param obj
     * @return
     */
    public static Map<String, Object> objCast2Map(Object obj) {
        if (null == obj) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        if (null == fields) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>(fields.length);
            Arrays.asList(fields).forEach(field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                try {
                    resultMap.put(fieldName, field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        return resultMap;
    }

}

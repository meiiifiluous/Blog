package com.vvc.utils;


import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    public static <O, T> T copyBean(O source, Class<T> clazz) {
        //创建目标对象
        T result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <O, T> List<T> copyBeanList(List<O> list, Class<T> clazz) {
        return list.stream().map(o -> copyBean(o, clazz)).collect(Collectors.toList());
    }
}

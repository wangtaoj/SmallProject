package com.waston.utils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wangtao
 * @date 2017-2017/12/1-0:44
 *
 * 获取bean对象中属性对应的set方法
 **/
public class MethodUtils {

    public static Method getMethod(Class<?> clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            String goal = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            if(Objects.equals(method.getName(), goal))
                return method;
        }
        return null;
    }

}

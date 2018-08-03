package com.waston.config;

/**
 * @author wangtao
 * @date 2017-2017/12/1-0:21
 *
 * 工厂接口, 获取容器中的bean对象
 **/
public interface BeanFactory {

    Object getBean(String id);

}

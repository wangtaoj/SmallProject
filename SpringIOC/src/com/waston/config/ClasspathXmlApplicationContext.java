package com.waston.config;

import com.waston.utils.MethodUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangtao
 * @date 2017-2017/12/1-0:24
 **/
public class ClasspathXmlApplicationContext implements BeanFactory {

    private Map<String, Bean> configMap;

    private Map<String, Object> context = new HashMap<>();

    //创建配置文件中的bean对象
    public ClasspathXmlApplicationContext(String path) {
        configMap = ConfigManager.parseConfig(path);
        for(Map.Entry<String, Bean> entry : configMap.entrySet()) {
            String beanId = entry.getKey();
            Bean bean = entry.getValue();

            Object obj = context.get(beanId);
            //因为createBean方法中也会将属性bean放入到容器中, 那么此时再遇到要创建时就不需要创建了
            //如果bean信息中scope为单例的才将对象放到容器中
            if(obj == null && Objects.equals(bean.getScope(), "singleton")) {
                obj = createBean(bean);
                context.put(beanId, obj);
            }
        }
    }

    private Object createBean(Bean bean) {
        Class<?> clazz;
        try {
            clazz = Class.forName(bean.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(bean.getClassName() + "没有找到");
        }

        //创建实例对象
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(bean.getClassName() + "类没有空参数构造方法");
        }

        //注入属性, 两种情况，8大基本类型， 引用类型
        for(Property property : bean.getProperties()) {
            String name = property.getName();
            String value = property.getValue();
            String ref = property.getRef();

            if(value != null) {
               Map<String, String[]> paramMap = new HashMap<>();
               paramMap.put(name, new String[]{value});
                try {
                    //使用Apache BeanUtils工具类注入属性，可以实现基本类型自动转化
                    BeanUtils.populate(obj, paramMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("请检查" + bean.getClassName() + "类的" + name + "属性!");
                }

            } else if(ref != null) {
                Object refObj = context.get(ref);
                //需要注入的属性bean不存在, 那么先从配置信息中找到该属性bean创建
                if(refObj == null) {
                    Bean refBean = configMap.get(ref);
                    //配置文件中引用属性写错了或者写成本身，即自身调用自己时抛出异常
                    if(refBean == null)
                        throw new RuntimeException("请检查配置文件!在初始化" + bean.getClassName() +
                                "类时没有发现它的引用属性--" + ref + "的配置信息");
                    else if(Objects.equals(ref, bean.getId()))
                        throw new RuntimeException("请检查配置文件!在初始化" + bean.getClassName() +
                                "类时正在试图使用自己来创建自己, 严重错误!");
                    else
                        refObj = createBean(refBean);
                    //单例才将bean对象放入到容器中
                    if(Objects.equals(configMap.get(ref).getScope(), "singleton"))
                        context.put(ref, refObj);
                }
                try {
                    //使用反射获取set方法
                    Method method = MethodUtils.getMethod(clazz, name);
                    if(method == null)
                        throw new RuntimeException(bean.getClassName() + "类中的" + name + "属性没有对应的set方法");
                    method.invoke(obj, refObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在初始化" + bean.getClassName() + "类中的" +
                            name + "属性时失败, 请检查set方法参数");
                }
            }

        }

        return obj;
    }

    @Override
    public Object getBean(String id) {
        Bean bean = configMap.get(id);
        //配置文件中没有对应的bean信息
        if(bean == null)
            return null;
        //非单例对象，用时创建
        if(!Objects.equals(bean.getScope(), "singleton"))
            return createBean(bean);
        return context.get(id);
    }
}

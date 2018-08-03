package com.waston.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangtao
 * @date 2017-2017/11/30-23:31
 **/
public class Bean {

    private String  id;

    private String className;

    private String scope = "singleton"; //容器默认单例创建对象

    private List<Property> properties = new ArrayList<>();

    public Bean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", scope='" + scope + '\'' +
                ", properties=" + properties +
                '}';
    }
}

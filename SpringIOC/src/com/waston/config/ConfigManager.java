package com.waston.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtao
 * @date 2017-2017/11/30-23:36
 **/
public class ConfigManager {

    public static Map<String, Bean> parseConfig(String path) {

        Map<String, Bean> configMap = new HashMap<>();

        //创建xml解析器
        SAXReader reader = new SAXReader();
        InputStream is = ConfigManager.class.getResourceAsStream(path);
        Document document;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("文件不符合xml规则，请检查配置文件!");
        }

        String xPath = "//bean";  //xPath表达式, 获取所有bean节点
        List<Element> elements = document.selectNodes(xPath);
        for(Element element : elements) {
            Bean bean = new Bean();
            bean.setId(element.attributeValue("id"));
            bean.setClassName(element.attributeValue("class"));
            String scope = element.attributeValue("scope");
            if(scope != null)
                bean.setScope(scope);
            List<Element> children = element.elements("property");
            for(Element child : children) {
                Property property = new Property();
                property.setName(child.attributeValue("name"));
                property.setValue(child.attributeValue("value"));
                property.setRef(child.attributeValue("ref"));
                bean.getProperties().add(property);
            }
            configMap.put(bean.getId(), bean);
        }
        return configMap;
    }

}

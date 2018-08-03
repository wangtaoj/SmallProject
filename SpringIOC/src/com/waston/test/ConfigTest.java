package com.waston.test;

import com.waston.bean.A;
import com.waston.bean.B;
import com.waston.config.Bean;
import com.waston.config.BeanFactory;
import com.waston.config.ClasspathXmlApplicationContext;
import com.waston.config.ConfigManager;
import org.junit.Test;

import java.util.Map;

/**
 * @author wangtao
 * @date 2017-2017/12/1-0:17
 **/
public class ConfigTest {

    /**
     * 测试xml文件配置加载解析是否正确
     */
    @Test
    public void testParseConfig() {
        Map<String, Bean> configMap = ConfigManager.parseConfig("/applicationContext.xml");
        System.out.println(configMap);
    }

    /**
     * 测试实例bean的创建，以及单例多例测试
     */
    @Test
    public void testCreateBean() {
        BeanFactory factory = new ClasspathXmlApplicationContext("/applicationContext.xml");
        A a = (A)factory.getBean("a");
        factory.getBean("a");
        factory.getBean("a");
        System.out.println(a);
        B b = (B)factory.getBean("b");
        factory.getBean("b");
        System.out.println(b);
    }

    /**
     * 测试值类型自动转化功能
     */
    @Test
    public void testConvert() {
        BeanFactory factory = new ClasspathXmlApplicationContext("/applicationContext.xml");
        A a = (A)factory.getBean("a");
        System.out.println(a);

        B b = (B)factory.getBean("b");
        System.out.println(b);
    }

    @Test
    public void testNPE() {
        BeanFactory factory = new ClasspathXmlApplicationContext("/applicationContext.xml");
        A a = (A)factory.getBean("cc");
        System.out.println(a);
    }

}

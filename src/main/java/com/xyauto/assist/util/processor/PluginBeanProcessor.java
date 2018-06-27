package com.xyauto.assist.util.processor;

import com.xyauto.assist.util.annotation.AutoRelease;
import com.xyauto.assist.util.scanner.PluginPathScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shiqm on 2017-07-18.
 */
@Configuration
public class PluginBeanProcessor implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        registerBean(beanDefinitionRegistry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    private void registerBean(BeanDefinitionRegistry registry) {
        PluginPathScanner pluginPathScanner = new PluginPathScanner(registry);
        pluginPathScanner.setAnnotationClass(AutoRelease.class);
        pluginPathScanner.registerFilters();
        pluginPathScanner.doScan("com.xyauto.assist.plugin");
    }


}

package com.xyauto.assist.util.scanner;

import com.xyauto.assist.util.Plugin;
import com.xyauto.assist.util.PluginCache;
import com.xyauto.assist.util.annotation.AutoRelease;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by shiqm on 2017-06-14.
 */
public class PluginPathScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;


    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public PluginPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    public void registerFilters() {
        if (this.annotationClass != null) {
            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
    }


    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(String.valueOf(definition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
            String beanName = beanNameGenerator.generateBeanName(definition, this.getRegistry());
            definition.setBeanClass(clazz);
            this.getRegistry().registerBeanDefinition(beanName, definition);
            AutoRelease autoRelease = clazz.getAnnotation(AutoRelease.class);
            Plugin plugin = new Plugin(autoRelease.value());
            PluginCache.add(beanName, plugin);
        }
        return beanDefinitions;
    }


}

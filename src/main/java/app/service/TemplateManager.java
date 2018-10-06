package app.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by landy on 2018/10/6.
 */
@Service
public class TemplateManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext application;
    private Map<String, AbstractTemplate> templateMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.application = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, AbstractTemplate> beans = application.getBeansOfType(AbstractTemplate.class);
        for (AbstractTemplate one : beans.values()) {
            templateMap.put(one.name(), one);
        }
    }

    public List<String> listTemplateName() {
        return Lists.newLinkedList(templateMap.keySet());
    }

    public AbstractTemplate getTemplate(String name) {
        return templateMap.get(name);
    }
}

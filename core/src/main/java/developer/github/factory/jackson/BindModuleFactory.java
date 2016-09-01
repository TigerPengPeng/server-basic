package developer.github.factory.jackson;

import com.fasterxml.jackson.databind.Module;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @package: developer.github.factory
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:34
 * @description:
 */
@Slf4j
public class BindModuleFactory {

    private Set<Module> modules = new HashSet<>();

    public BindModuleFactory(String moduleString) {
        List<String> array = getModulesClassName(moduleString);
        if (CollectionUtils.isEmpty(array)) {
            return;
        }

        try {
            for (String module : array) {
                Class clazz = Class.forName(module);
                Object object = clazz.newInstance();
                if (object instanceof Module) {
                    modules.add((Module) object);
                }
            }
        } catch (Throwable t) {
            log.error("{}", t);
            throw new IllegalArgumentException(ExceptionUtils.getFullStackTrace(t));
        }
    }

    private List<String> getModulesClassName(String moduleString) {
        if (StringUtils.isEmpty(moduleString)) {
            return null;
        }
        String[] array = moduleString.split(",");
        if (array.length == 0) {
            return null;
        }

        List<String> modules = new ArrayList<>();
        for (String item : array) {
            if (StringUtils.isEmpty(item)) {
                continue;
            }
            modules.add(item.trim());
        }
        return modules;
    }

    public Set<Module> get() {
        return modules;
    }

    @PreDestroy
    public void destroy() {
        modules = null;
    }
}

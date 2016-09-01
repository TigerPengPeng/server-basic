package developer.github.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import developer.github.factory.jackson.BindModuleFactory;
import developer.github.jackson.UserMetaObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;

/**
 * @package: developer.github.factory
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:12
 * @description:
 */
@Slf4j
public class UserMetaObjectMapperFactory {
    @Autowired
    @Qualifier("bindModuleFactory")
    private BindModuleFactory bindModuleFactory;

    private ObjectMapper objectMapper;

    public UserMetaObjectMapperFactory(ObjectMapper _objectMapper) {
        objectMapper = new UserMetaObjectMapper(_objectMapper);
    }

    public ObjectMapper get() {
        if (!CollectionUtils.isEmpty(bindModuleFactory.get())) {
            objectMapper.registerModules(bindModuleFactory.get());
        }
        return objectMapper;
    }

    @PreDestroy
    public void destroy() {
        objectMapper = null;
    }
}

package developer.github.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @package: developer.github.jackson
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:38
 * @description:
 */
public class UserMetaModule extends SimpleModule {

    public UserMetaModule() {
        setSerializerModifier(new UserMetaBeanSerializerModifier());
    }
}
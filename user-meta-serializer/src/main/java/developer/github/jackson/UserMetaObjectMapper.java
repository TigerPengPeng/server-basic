package developer.github.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

/**
 * @package: developer.github.jackson
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:01
 * @description:
 */
public class UserMetaObjectMapper extends ObjectMapper {

    public UserMetaObjectMapper() {
        super();

        DefaultSerializerProvider userMetaSerializerProvider = new UserMetaSerializerProvider();
        this.setSerializerProvider(userMetaSerializerProvider);
    }

    public UserMetaObjectMapper(ObjectMapper _objectMapper) {
        super(_objectMapper);

        DefaultSerializerProvider userMetaSerializerProvider = new UserMetaSerializerProvider();
        this.setSerializerProvider(userMetaSerializerProvider);
    }
}

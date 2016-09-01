package developer.github.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @package: developer.github.jackson
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:02
 * @description:
 */
@Slf4j
public class UserMetaSerializerProvider extends DefaultSerializerProvider {

    public UserMetaSerializerProvider() {
        super();
    }

    public UserMetaSerializerProvider(UserMetaSerializerProvider src,
                                      SerializationConfig config, SerializerFactory f) {
        super(src, config, f);
    }

    /**
     * Overridable method, used to create a non-blueprint instances from the blueprint.
     * This is needed to retain state during serialization.
     *
     * @param config
     * @param jsf
     */
    @Override
    public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new UserMetaSerializerProvider(this, config, jsf);
    }

    /**
     * The method to be called by {@link ObjectMapper} and {@link ObjectWriter}
     * for serializing given value, using serializers that
     * this provider has access to (via caching and/or creating new serializers
     * as need be).
     */
    @Override
    public void serializeValue(JsonGenerator jgen, Object value) throws IOException {
        log.info("UserMetaSerializerProvider.serializeValue: {}", value);
        super.serializeValue(jgen, value);
    }
}

package developer.github.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @package: developer.github.jackson
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午7:14
 * @description:
 */
public class UserMetaBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (serializer instanceof BeanSerializer) {
            return new UserMetaBeanSerializer((BeanSerializer) serializer);
        } else {
            return serializer;
        }
    }
}

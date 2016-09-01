package developer.github.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import developer.github.annotation.UserMeta;
import developer.github.core.CoreContext;
import developer.github.model.UserMetaVO;
import developer.github.service.UserMetaCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @package: developer.github.jackson
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午7:16
 * @description:
 */
@Slf4j
public class UserMetaBeanSerializer extends BeanSerializer {

    /**
     * Alternate copy constructor that can be used to construct
     * standard {@link BeanSerializer} passing an instance of
     * "compatible enough" source serializer.
     */
    public UserMetaBeanSerializer(final BeanSerializer src) {
        super(src);
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        log.info("UserMetaBeanSerializer.serializeFields: {}", bean);
        super.serializeFields(bean, jgen, provider);

        final BeanPropertyWriter[] props;
        if (_filteredProps != null && provider.getActiveView() != null) {
            props = _filteredProps;
        } else {
            props = _props;
        }

        List<BeanPropertyWriter> userMetaProps = getUserMetaProperties(props);
        List<String> propValues = null;
        try {
            propValues = getPropertyValue(bean, userMetaProps);
        } catch (Throwable t) {
            log.error("{}", t);
        }

        UserMetaCacheService userMetaCacheService = CoreContext.getBean("userMetaCacheService", UserMetaCacheService.class);
        Map<String, UserMetaVO> userMetaKeyValuePair = userMetaCacheService.getKeyValuePair(propValues);
        writeUserMetaJson(bean, jgen, userMetaProps, userMetaKeyValuePair);
    }

    /**
     * Alternative serialization method that gets called when there is a
     * {@link PropertyFilter} that needs to be called to determine
     * which properties are to be serialized (and possibly how)
     */
    @Override
    protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        log.info("UserMetaBeanSerializer.serializeFieldsFiltered: {}", bean);
        super.serializeFields(bean, jgen, provider);

        final BeanPropertyWriter[] props;
        if (_filteredProps != null && provider.getActiveView() != null) {
            props = _filteredProps;
        } else {
            props = _props;
        }
        final PropertyFilter filter = findPropertyFilter(provider, _propertyFilterId, bean);
        if (filter == null) {
            serializeFields(bean, jgen, provider);
            return;
        }

        List<BeanPropertyWriter> userMetaProps = getUserMetaProperties(props);
        List<String> propValues = null;
        try {
            propValues = getPropertyValue(bean, userMetaProps);
        } catch (Throwable t) {
            log.error("{}", t);
        }

        UserMetaCacheService userMetaCacheService = CoreContext.getBean("userMetaCacheService", UserMetaCacheService.class);
        Map<String, UserMetaVO> userMetaKeyValuePair = userMetaCacheService.getKeyValuePair(propValues);
        writeUserMetaJson(bean, jgen, userMetaProps, userMetaKeyValuePair);
    }

    /**
     * write user-meta json to bean
     * @param bean
     * @param jgen
     * @param userMetaProps
     * @param userMetaKeyValuePair
     */
    private void writeUserMetaJson(Object bean, JsonGenerator jgen, List<BeanPropertyWriter> userMetaProps, Map<String, UserMetaVO> userMetaKeyValuePair) {
        if (userMetaKeyValuePair == null || userMetaKeyValuePair.isEmpty()) {
            return;
        }

        for (BeanPropertyWriter prop : userMetaProps) {
            try {
                Object value = prop.get(bean);
                if (value == null) {
                    continue;
                }

                String userMetaName = prop.getAnnotation(UserMeta.class).name();
                if (value.getClass().isArray() || value instanceof Collection) {
                    jgen.writeFieldName(userMetaName);
                    List<UserMetaVO> userMetaVOs = getMultipleItems(userMetaKeyValuePair, getPropertyValue(bean, prop));
                    if (CollectionUtils.isEmpty(userMetaVOs)) {
                        jgen.writeStartArray();
                        jgen.writeEndArray();
                    } else {
                        jgen.writeObject(userMetaVOs);
                    }

                } else {
                    jgen.writeObjectField(userMetaName, userMetaKeyValuePair.get(String.valueOf(value)));
                }

            } catch (Throwable t) {
                log.error("{}", t);
            }
        }
    }


    private List<BeanPropertyWriter> getUserMetaProperties(final BeanPropertyWriter[] props) {
        List<BeanPropertyWriter> userMetaProps = new ArrayList<>(props.length);
        for (int i = 0; i < props.length; i++) {
            BeanPropertyWriter prop = props[i];
            if (prop == null) {
                continue;
            }
            UserMeta userMeta = prop.getAnnotation(UserMeta.class);
            if (userMeta != null) {
                userMetaProps.add(prop);
            }
        }
        return userMetaProps;
    }

    /**
     * 获取bean的userMetaProps的值的集合(带去重)
     * @param bean
     * @param userMetaProps
     * @return
     * @throws Throwable
     */
    private List<String> getPropertyValue(final Object bean, final List<BeanPropertyWriter> userMetaProps) throws  Throwable {
        if (CollectionUtils.isEmpty(userMetaProps)) {
            return new ArrayList<>();
        }

        Set<String> values = new HashSet<>();
        for (BeanPropertyWriter prop : userMetaProps) {
            List<String> propValues = getPropertyValue(bean, prop);
            if (!CollectionUtils.isEmpty(propValues)) {
                values.addAll(propValues);
            }
        }
        return new ArrayList<>(values);
    }

    private List<String> getPropertyValue(final Object bean, BeanPropertyWriter userMetaProp) throws Throwable {
        if (userMetaProp == null) {
            return null;
        }

        Object value = userMetaProp.get(bean);
        if (value == null) {
            return null;
        }

        List<String> values = new LinkedList<>();
        if (value.getClass().isArray()) {
            Object[] arrays = (Object[]) value;
            for (Object item : arrays) {
                values.add(String.valueOf(item));
            }

        } else {
            if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                for (Object item : collection) {
                    values.add(String.valueOf(item));
                }

            } else {
                values.add(String.valueOf(value));
            }
        }
        return  values;
    }

    private <T> List<T> getMultipleItems(Map<String, T> map, List<String> keys) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }

        List<T> values = new LinkedList<>();
        for (String key : keys) {
            T item = map.get(key);
            if (item != null) {
                values.add(item);
            }
        }
        return values;
    }
}

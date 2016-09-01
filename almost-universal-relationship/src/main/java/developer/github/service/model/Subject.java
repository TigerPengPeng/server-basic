package developer.github.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @class:
 * @author: 黄鹏
 * @description:
 */
public class Subject {

    @Getter
    private Object object;

    @Getter
    private Class<?> clazz;

    @JsonIgnore
    @Getter
    private RelatedType relatedType;

    private Subject() {}

    public Subject(Object _object, RelatedType _relatedType ) {
        if (_object == null) {
            throw new IllegalArgumentException("_object can not be null");
        }
        object = _object;
        relatedType = _relatedType;
        clazz = object.getClass();
    }

    public <T> T getObjectValue(Class<T> _clazz) {
        if (!clazz.equals(_clazz)) {
            throw new IllegalArgumentException("_clazz not equals to object.clazz");
        }
        return (T) object;
    }

    public static List<Subject> makeObjectSubjects(Object[] array, RelatedType relatedType) {
        if (array == null || array.length == 0) {
            return new ArrayList<>();
        }
        return makeObjectSubjects(Arrays.asList(array), relatedType);
    }

    public static List<Subject> makeObjectSubjects(Collection<?> objects, RelatedType relatedType) {
        if (relatedType == null) {
            throw new IllegalArgumentException("relatedType can not be null");
        }
        if (CollectionUtils.isEmpty(objects)) {
            return new ArrayList<>();
        }

        List<Subject> subjects = new ArrayList<>(objects.size());
        for (Object o : objects) {
            if (o == null) {
                continue;
            }
            Subject subject = new Subject(o, relatedType);
            subjects.add(subject);
        }
        return subjects;
    }

}

package developer.github.utils;

import developer.github.service.annotation.RelatedField;
import developer.github.service.annotation.RelatedObject;
import developer.github.service.model.RelatedType;
import developer.github.service.model.Relation;
import developer.github.service.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年07月05日 下午1:42
 * @description:
 */
@Slf4j
public class RelationUtils {

    private static Set<Field> getAnnotatedField(Class<?> clazz) {
        Set<? extends Class<?>> supers = ReflectionUtils.getSuperClasses(clazz);
        Set<Field> fields = new HashSet<>();

        for (Class<?> superClass : supers) {
            for (Field field : superClass.getDeclaredFields()) {
                if (field.getAnnotation(RelatedField.class) != null) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    public static List<Relation> getObjectRelations(Object target) {
        Long objectId = ReflectionUtils.getFieldValue(target, "id");
        return getObjectRelations(objectId, target);
    }

    public static List<Relation> getObjectRelations(Long objectId, Object target) {
        return getObjectRelations(objectId, target, null);
    }

    /**
     *
     * @param objectId
     * @param target
     * @param relatedTypes  if null or empty, insert all annotated fields.
     * @return
     */
    public static List<Relation> getObjectRelations(Long objectId, Object target, Collection<RelatedType> relatedTypes) {
        if (objectId == null) {
            throw new IllegalArgumentException("object must have id field");
        }
        if (target == null) {
            return new ArrayList<>();
        }

        Class<?> clazz = target.getClass();
        RelatedObject relatedObject = clazz.getAnnotation(RelatedObject.class);

        Set<Field> fields = getAnnotatedField(clazz);
        log.info("getAnnotatedField: {}", fields);
        if (relatedObject == null || CollectionUtils.isEmpty(fields)) {
            return new ArrayList<>();
        }

        List<Subject> subjects = new LinkedList<>();
        for (Field field : fields) {
            RelatedField relatedField = field.getAnnotation(RelatedField.class);
            if (relatedField.relatedType() == null) {
                continue;
            }
            if (!CollectionUtils.isEmpty(relatedTypes) && !relatedTypes.contains(relatedField.relatedType())) {
                continue;
            }

            Object value = null;
            try {
                value = field.get(target);
            } catch (IllegalAccessException e) {
                log.error("{}", e);
            }
            if (value == null) {
                continue;
            }
            log.info("value {}", value);

            List<Subject> list;
            if (value.getClass().isArray()) {
                Object[] arrays = (Object[]) value;
                list = Subject.makeObjectSubjects(arrays, relatedField.relatedType());
            } else {
                if (value instanceof Collection) {
                    list = Subject.makeObjectSubjects((Collection<?>) value, relatedField.relatedType());
                } else {
                    list = Subject.makeObjectSubjects(Arrays.asList(value), relatedField.relatedType());
                }
            }
            subjects.addAll(list);
        }

        log.info("subjects: {}", subjects);
        List<Relation> targetRelates = new ArrayList<>(subjects.size());
        for (Subject subject : subjects) {
            Relation relation = new Relation(subject);
            relation.setObjectId(objectId);
            relation.setObjectType(relatedObject.objectType());
            targetRelates.add(relation);
        }
        return targetRelates;
    }

    public static Map<RelatedType, List<Subject>> makeRelationsAsMap(Collection<Relation> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return new HashMap<>();
        }

        Map<RelatedType, List<Subject>> map = new HashMap<>();
        for (Relation relation : relations) {
            RelatedType relatedType = relation.getRelatedType();
            List<Subject> list = map.get(relatedType);
            if (list == null) {
                list = new LinkedList<>();

            }
            list.add(relation.getRelated());
            map.put(relatedType, list);
        }
        return map;
    }

    public static List<Object> makeSubjectValueArray(Collection<Subject> subjects) {
        if (CollectionUtils.isEmpty(subjects)) {
            return new ArrayList<>();
        }

        Class<?> subjectClazz = subjects.iterator().next().getClazz();
        List<Object> list = new ArrayList<>(subjects.size());

        for (Subject subject : subjects) {
            Object object = subject.getObjectValue(subjectClazz);
            list.add(object);
        }
        return list;
    }

    public static <T> List<T> parseObjectsFields(List<T> objects, Collection<Relation> multipleRelations) {
        if (CollectionUtils.isEmpty(objects)) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(multipleRelations)) {
            return objects;
        }

        Map<Long, List<Relation>> map = new HashMap<>();
        for (Relation relation : multipleRelations) {
            Long objectId = relation.getObjectId();
            List<Relation> relations = map.get(objectId);
            if (relations == null) {
                relations = new ArrayList<>();
            }
            relations.add(relation);
            map.put(objectId, relations);
        }

        for (T object : objects) {
            Long objectId = ReflectionUtils.getFieldValue(object, "id");
            if (objectId == null) {
                throw new IllegalArgumentException("object must own field named id");
            }
            parseObjectFields(objectId, object, map.get(objectId));
        }
        return objects;
    }


    public static <T> T parseObjectFields(T object, Collection<Relation> relations) {
        Long objectId = ReflectionUtils.getFieldValue(object, "id");
        return parseObjectFields(objectId, object, relations);
    }

    public static <T> T parseObjectFields(Long objectId, T object, Collection<Relation> relations) {
        if (objectId == null) {
            throw new IllegalArgumentException("object must own field named id");
        }
        if (object == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(relations)) {
            return object;
        }

        Map<String, Field> fields = ReflectionUtils.getClassFields(object.getClass(), true);
        Map<RelatedType, List<Subject>> map = RelationUtils.makeRelationsAsMap(relations);

        for (RelatedType relatedType : map.keySet()) {
            List<Subject> subjects = map.get(relatedType);
            if (CollectionUtils.isEmpty(subjects)) {
                continue;
            }

            Field field = fields.get(relatedType.getField());
            if (field == null) {
                continue;
            }

            RelatedField relatedFieldAnnotation = field.getAnnotation(RelatedField.class);
            Class<?> runtimeClass = relatedFieldAnnotation.runtimeClass();
            if (runtimeClass.isArray() || field.getType().isArray()) {
                throw new RuntimeException("array type can not be supported");
            }

            Object fieldInstance = null;
            try {
                fieldInstance = runtimeClass.newInstance();
            } catch (Throwable t) {
                log.error("{}", t);
            }
            if (fieldInstance == null) {
                throw new RuntimeException("runtimeClass.newInstance() failed, can not new instance with default no arguments constructor");
            }

            if (fieldInstance instanceof Collection) {
                Collection value = (Collection) fieldInstance;
                List<Object> list = RelationUtils.makeSubjectValueArray(subjects);
                value.addAll(list);
            } else {
                Subject subject = subjects.iterator().next();
                Class<?> subjectClazz = subject.getClazz();
                fieldInstance = subject.getObjectValue(subjectClazz);
            }

            try {
                field.set(object, fieldInstance);
            } catch (IllegalAccessException e) {
                log.error("{}", e);
            }
        }
        return object;
    }
}

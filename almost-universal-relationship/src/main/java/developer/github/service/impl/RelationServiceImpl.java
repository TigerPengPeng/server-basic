package developer.github.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import developer.github.async.AsyncProducer;
import developer.github.async.event.RemoveRelationEvent;
import developer.github.dao.RelationDao;
import developer.github.service.RelationService;
import developer.github.service.model.RelatedType;
import developer.github.service.model.Relation;
import developer.github.utils.ReflectionUtils;
import developer.github.utils.RelationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @package: developer.github.service.impl
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午2:56
 * @description:
 */
@Service("relationService")
@Slf4j
public class RelationServiceImpl implements RelationService {
    @Autowired
    @Qualifier("relationDao")
    private RelationDao relationDao;

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("relationAsyncProducer")
    private AsyncProducer producer;

    /**
     * 全量保存object对象的关联属性, 同时将object对象保存到mongo中(根据id去重)
     * 提取object对象的annotations && object.id
     * @RelatedObject: class上的annotations, 获取class的object_type
     * @RelatedField: field上的annotations
     * @param object
     * @return
     */
    @Override
    public <T> int save(T object) {
        log.info("relation service save: {}", object);
        if (object == null) {
            return 0;
        }
        Long objectId = ReflectionUtils.getFieldValue(object, "id");
        if (objectId == null) {
            throw new IllegalArgumentException("object must have id field");
        }

        List<Relation> relations = RelationUtils.getObjectRelations(objectId, object);
        log.info("object relations: {}", relations);
        relationDao.deleteObjectRelation(objectId);
        int insert = relationDao.batchInsert(relations);

        RemoveRelationEvent event = new RemoveRelationEvent(objectId);
        producer.durabilityPublish(event);
        return insert;
    }

    /**
     * 增量编辑
     *
     * @param object
     * @param relatedTypes
     * @return
     */
    @Override
    public <T> int add(T object, Collection<RelatedType> relatedTypes) {
        log.info("relation service add: {}", object);
        if (CollectionUtils.isEmpty(relatedTypes)) {
            throw new IllegalArgumentException("relatedTypes can not be null or empty");
        }
        if (object == null) {
            return 0;
        }
        Long objectId = ReflectionUtils.getFieldValue(object, "id");
        if (objectId == null) {
            throw new IllegalArgumentException("object must have id field");
        }

        List<Relation> relations = RelationUtils.getObjectRelations(objectId, object, relatedTypes);
        return relationDao.batchInsert(relations);
    }

    /**
     * 增量编辑
     *
     * @param object
     * @param relatedType
     * @return
     */
    @Override
    public <T> int add(T object, RelatedType relatedType) {
        if (relatedType == null) {
            throw new IllegalArgumentException("relatedType can not be null");
        }
        return add(object, Arrays.asList(relatedType));
    }

    /**
     * 单量编辑
     *
     * @param object
     * @param relatedType
     * @return
     */
    @Override
    public <T> int update(T object, RelatedType relatedType) {
        if (relatedType == null) {
            throw new IllegalArgumentException("relatedType can not be null");
        }
        return update(object, Arrays.asList(relatedType));
    }

    /**
     * 单量编辑
     *
     * @param object
     * @param relatedTypes
     * @return
     */
    @Override
    public <T> int update(T object, Collection<RelatedType> relatedTypes) {
        log.info("relation service update: {}", object);
        if (CollectionUtils.isEmpty(relatedTypes)) {
            throw new IllegalArgumentException("relatedTypes can not be null or empty");
        }
        if (object == null) {
            return 0;
        }
        Long objectId = ReflectionUtils.getFieldValue(object, "id");
        if (objectId == null) {
            throw new IllegalArgumentException("object must have id field");
        }

        List<Relation> relations = RelationUtils.getObjectRelations(objectId, object, relatedTypes);
        relationDao.deleteObjectRelation(objectId, relatedTypes);
        return relationDao.batchInsert(relations);
    }

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     *
     * @param objects
     * @return
     */
    @Override
    public <T> List<T> parseObjectRelations(List<T> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return new ArrayList<>();
        }

        log.info("relation service set relations: {}", objects);
        List<Long> objectIds = new ArrayList<>(objects.size());
        for (T object : objects) {
            Long id = ReflectionUtils.getFieldValue(object, "id");
            if (id == null) {
                throw new IllegalArgumentException("object must own field named id");
            }
            objectIds.add(id);
        }

        List<Relation> relations = relationDao.selectObjectRelations(objectIds);
        return RelationUtils.parseObjectsFields(objects, relations);
    }

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     *
     * @param object
     * @return
     */
    @Override
    public <T> T parseObjectRelations(T object) {
        if (object == null) {
            return null;
        }
        log.info("relation service set relations: {}", object);

        Map<String, Field> fields = ReflectionUtils.getClassFields(object.getClass(), true);
        Long objectId = ReflectionUtils.getFieldValue(fields.get("id"), object);
        if (objectId == null) {
            throw new IllegalArgumentException("object must own field named id");
        }

        List<Relation> relations = relationDao.selectObjectRelations(objectId);
        return RelationUtils.parseObjectFields(objectId, object, relations);
    }

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     *
     * @param object
     * @param relatedTypes
     * @return
     */
    @Override
    public <T> T parseObjectRelations(T object, RelatedType... relatedTypes) {
        if (object == null) {
            return null;
        }
        if (relatedTypes == null || relatedTypes.length == 0) {
            return parseObjectRelations(object);
        }

        log.info("relation service set relations: {} with relatedTypes: {}", object, relatedTypes);
        Map<String, Field> fields = ReflectionUtils.getClassFields(object.getClass(), true);
        Long objectId = ReflectionUtils.getFieldValue(fields.get("id"), object);
        if (objectId == null) {
            throw new IllegalArgumentException("object must own field named id");
        }

        List<Relation> relations = relationDao.selectObjectRelations(objectId, relatedTypes);
        return RelationUtils.parseObjectFields(objectId, object, relations);
    }
}

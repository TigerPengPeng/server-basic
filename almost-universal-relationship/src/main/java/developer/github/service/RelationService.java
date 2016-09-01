package developer.github.service;

import developer.github.service.model.RelatedType;

import java.util.Collection;
import java.util.List;

/**
 * @package: developer.github.service
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午2:56
 * @description:
 */
public interface RelationService {
    /**
     * 全量保存object对象的关联属性, 同时将object对象保存到mongo中(根据id去重)
     * 提取object对象的annotations && object.id
     * @RelatedObject: class上的annotations, 获取class的object_type
     * @RelatedField: field上的annotations
     * @param object
     * @return
     */
    <T> int save(T object);

    /**
     * 增量编辑
     * @param object
     * @param relatedTypes
     * @param <T>
     * @return
     */
    <T> int add(T object, Collection<RelatedType> relatedTypes);

    /**
     * 增量编辑
     * @param object
     * @param relatedType
     * @param <T>
     * @return
     */
    <T> int add(T object, RelatedType relatedType);

    /**
     * 单量编辑
     * @param object
     * @param relatedType
     * @param <T>
     * @return
     */
    <T> int update(T object, RelatedType relatedType);

    /**
     * 单量编辑
     * @param object
     * @param relatedTypes
     * @param <T>
     * @return
     */
    <T> int update(T object, Collection<RelatedType> relatedTypes);

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     * @param objects
     * @param <T>
     * @return
     */
    <T> List<T> parseObjectRelations(List<T> objects);

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     * @param object
     * @param <T>
     * @return
     */
    <T> T parseObjectRelations(T object);

    /**
     * 根据relations表中的数据, set成object的对应的field的值
     * @param object
     * @param relatedTypes
     * @param <T>
     * @return
     */
    <T> T parseObjectRelations(T object, RelatedType... relatedTypes);
}

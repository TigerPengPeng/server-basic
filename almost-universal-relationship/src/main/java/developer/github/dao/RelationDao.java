package developer.github.dao;

import developer.github.service.model.RelatedType;
import developer.github.service.model.Relation;

import java.util.Collection;
import java.util.List;

/**
 * @package: developer.github.dao
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午2:56
 * @description:
 */
public interface RelationDao {
    Long insertSelective(Relation relation);

    int batchInsert(Collection<Relation> relations);

    int deleteObjectRelation(Long objectId);

    int deleteObjectRelation(Long objectId, Collection<RelatedType> relatedTypes);

    List<Relation> selectObjectRelations(Long objectId);

    List<Relation> selectObjectRelations(List<Long> objectIds);

    List<Relation> selectObjectRelations(Long objectId, RelatedType relatedType);

    List<Relation> selectObjectRelations(Long objectId, Collection<RelatedType> relatedTypes);

    List<Relation> selectObjectRelations(Long objectId, RelatedType... relatedTypes);
}

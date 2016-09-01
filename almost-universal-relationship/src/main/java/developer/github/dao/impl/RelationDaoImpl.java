package developer.github.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import developer.github.dao.RelationDao;
import developer.github.dao.model.BasicStatus;
import developer.github.dao.model.RelationDaoModel;
import developer.github.dao.model.RelationExample;
import developer.github.mapper.RelationMapper;
import developer.github.service.model.ObjectType;
import developer.github.service.model.RelatedType;
import developer.github.service.model.Relation;
import developer.github.service.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @package: developer.github.dao.impl
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午2:56
 * @description:
 */
@Component("relationDao")
@Slf4j
public class RelationDaoImpl implements RelationDao {
    @Autowired
    @Qualifier("relationMapper")
    private RelationMapper mapper;

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper objectMapper;

    @Override
    public Long insertSelective(Relation relation) {
        relation.setCreatedAt(new Date());
        relation.setUpdatedAt(new Date());
        relation.setStatus(BasicStatus.EXISTS.getType());
        RelationDaoModel model = transferToMapper(relation);
        if (model == null) {
            return 0L;
        }
        mapper.insertSelective(model);
        relation.setId(model.getId());
        return model.getId();
    }

    @Override
    public int batchInsert(Collection<Relation> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return 0;
        }
        List<RelationDaoModel> daoModels = new ArrayList<>(relations.size());
        for (Relation relation : relations) {
            relation.setCreatedAt(new Date());
            relation.setUpdatedAt(new Date());
            relation.setStatus(BasicStatus.EXISTS.getType());
            RelationDaoModel model = transferToMapper(relation);
            if (model != null) {
                daoModels.add(model);
            }
        }
        return mapper.batchInsert(daoModels);
    }

    @Override
    public int deleteObjectRelation(Long objectId) {
        if (objectId == null) {
            return 0;
        }

        RelationDaoModel model = new RelationDaoModel();
        model.setUpdatedAt(new Date());
        model.setStatus(BasicStatus.DELETE.getType());

        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.EXISTS.getType());
        return mapper.updateByExampleSelective(model, example);
    }

    @Override
    public int deleteObjectRelation(Long objectId, Collection<RelatedType> relatedTypes) {
        if (objectId == null || CollectionUtils.isEmpty(relatedTypes)) {
            return 0;
        }

        RelationDaoModel model = new RelationDaoModel();
        model.setUpdatedAt(new Date());
        model.setStatus(BasicStatus.DELETE.getType());

        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.EXISTS.getType())
                .andRelatedTypeIn(RelatedType.parseIntegerList(relatedTypes));
        return mapper.updateByExampleSelective(model, example);
    }

    @Override
    public List<Relation> selectObjectRelations(Long objectId) {
        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.EXISTS.getType());
        List<RelationDaoModel> models = mapper.selectByExample(example);
        return transferToService(models);
    }

    @Override
    public List<Relation> selectObjectRelations(List<Long> objectIds) {
        if (CollectionUtils.isEmpty(objectIds)) {
            return new ArrayList<>();
        }

        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdIn(objectIds)
                .andStatusEqualTo(BasicStatus.EXISTS.getType());
        List<RelationDaoModel> models = mapper.selectByExample(example);
        return transferToService(models);
    }

    @Override
    public List<Relation> selectObjectRelations(Long objectId, RelatedType relatedType) {
        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.EXISTS.getType())
                .andRelatedTypeEqualTo(relatedType.getType());
        List<RelationDaoModel> models = mapper.selectByExample(example);
        return transferToService(models);
    }

    @Override
    public List<Relation> selectObjectRelations(Long objectId, Collection<RelatedType> relatedTypes) {
        RelationExample example = new RelationExample();
        RelationExample.Criteria criteria = example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.EXISTS.getType());
        if (!CollectionUtils.isEmpty(relatedTypes)) {
            criteria.andRelatedTypeIn(RelatedType.parseIntegerList(relatedTypes));
        }
        List<RelationDaoModel> models = mapper.selectByExample(example);
        return transferToService(models);
    }

    @Override
    public List<Relation> selectObjectRelations(Long objectId, RelatedType... relatedTypes) {
        if (relatedTypes == null || relatedTypes.length == 0) {
            return selectObjectRelations(objectId);
        }
        List<RelatedType> list = Arrays.asList(relatedTypes);
        return selectObjectRelations(objectId, list);
    }

    private List<Relation> transferToService(Collection<RelationDaoModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return new ArrayList<>();
        }
        List<Relation> items = new ArrayList<>(models.size());
        for (RelationDaoModel model : models) {
            Relation relation = transferToService(model);
            if (relation != null) {
                items.add(relation);
            }
        }
        return items;
    }

    private Relation transferToService(RelationDaoModel model) {
        if (model == null) {
            return null;
        }

        Subject subject = null;
        try {
            JSONObject jsonObject = new JSONObject(model.getRelated());
            Class<?> clazz = Class.forName(jsonObject.getString("clazz"));
            Object objectValue = jsonObject.get("object");
            Object object;
            if (clazz.equals(String.class)) {
                object = objectValue;
            } else {
                object = objectMapper.readValue(objectValue.toString(), clazz);
            }
            subject = new Subject(object, RelatedType.getByType(model.getRelatedType()));

        } catch (Throwable t) {
            log.error("{}", t);
        }

        if (subject == null) {
            return null;
        }

        Relation item = new Relation(subject);
        item.setId(model.getId());
        item.setCreatedById(model.getCreatedById());
        item.setCreatedAt(model.getCreatedAt());
        item.setUpdatedById(model.getUpdatedById());
        item.setUpdatedAt(model.getUpdatedAt());
        item.setVersion(model.getVersion());
        item.setStatus(model.getStatus());
        item.setObjectId(model.getObjectId());
        item.setObjectType(ObjectType.getByType(model.getObjectType()));
        item.setRelatedString(model.getRelated());
        return item;
    }

    private List<RelationDaoModel> transferToMapper(Collection<Relation> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return new ArrayList<>();
        }
        List<RelationDaoModel> models = new ArrayList<>(relations.size());
        for (Relation relation : relations) {
            RelationDaoModel model = transferToMapper(relation);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    private RelationDaoModel transferToMapper(Relation relation) {
        if (relation == null) {
            return null;
        }
        if (!relation.validate()) {
            throw new IllegalArgumentException("Relation object validate false");
        }
        RelationDaoModel model = new RelationDaoModel();
        model.setId(relation.getId());
        model.setCreatedById(relation.getCreatedById());
        model.setCreatedAt(relation.getCreatedAt());
        model.setUpdatedById(relation.getUpdatedById());
        model.setUpdatedAt(relation.getUpdatedAt());
        model.setVersion(relation.getVersion());
        model.setStatus(relation.getStatus());
        model.setObjectId(relation.getObjectId());
        model.setObjectType(relation.getObjectType().getType());

        try {
            model.setRelated(objectMapper.writeValueAsString(relation.getRelated()));
        } catch (JsonProcessingException e) {
            log.error("{}", e);
        }

        model.setRelatedType(relation.getRelatedType().getType());
        return model;
    }
}

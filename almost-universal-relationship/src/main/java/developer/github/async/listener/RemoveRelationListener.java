package developer.github.async.listener;

import developer.github.annotaion.Async;
import developer.github.async.AsyncAbstractListener;
import developer.github.async.event.RemoveRelationEvent;
import developer.github.dao.model.BasicStatus;
import developer.github.dao.model.RelationDaoModel;
import developer.github.dao.model.RelationExample;
import developer.github.mapper.RelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @package: developer.github.async.listener
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午4:03
 * @description:
 */
@Component("removeRelationListener")
@Slf4j
public class RemoveRelationListener extends AsyncAbstractListener {

    @Autowired
    @Qualifier("relationMapper")
    private RelationMapper mapper;

    @Async
    public void removeRelationEventListener(RemoveRelationEvent event) {
        Long objectId = event.getObjectId();
        RelationExample example = new RelationExample();
        example.createCriteria()
                .andObjectIdEqualTo(objectId)
                .andStatusEqualTo(BasicStatus.DELETE.getType());
        List<RelationDaoModel> models = mapper.selectByExample(example);
        if (CollectionUtils.isEmpty(models)) {
            return;
        }
        mapper.batchInsertRemoveRelation(models);
        mapper.deleteByExample(example);
    }
}

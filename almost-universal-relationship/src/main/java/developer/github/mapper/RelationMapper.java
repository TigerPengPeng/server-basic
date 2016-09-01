package developer.github.mapper;

import developer.github.dao.model.RelationDaoModel;
import developer.github.dao.model.RelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RelationMapper {
    int countByExample(RelationExample example);

    int insert(RelationDaoModel record);

    int batchInsert(List<RelationDaoModel> list);

    int batchInsertRemoveRelation(List<RelationDaoModel> list);

    int insertSelective(RelationDaoModel record);

    List<RelationDaoModel> selectByExample(RelationExample example);

    RelationDaoModel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RelationDaoModel record, @Param("example") RelationExample example);

    int updateByExample(@Param("record") RelationDaoModel record, @Param("example") RelationExample example);

    int updateByPrimaryKeySelective(RelationDaoModel record);

    int updateByPrimaryKey(RelationDaoModel record);

    int deleteByExample(RelationExample example);
}
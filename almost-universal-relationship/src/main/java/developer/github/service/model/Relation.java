package developer.github.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午2:57
 * @description:
 */
@Data
public class Relation {
    private Long id;

    private Long createdById;

    private Date createdAt;

    private Long updatedById;

    private Date updatedAt;

    private Long version;

    private Integer status;

    private Long objectId;

    private ObjectType objectType;

    /**
     * 从数据库查询出的related字段的值
     */
    @JsonIgnore
    private String relatedString;

    private Subject related;

    private RelatedType relatedType;

    public boolean validate() {
        if (relatedType == null || related == null) {
            return false;
        }
        if (objectType == null || objectId == null) {
            return false;
        }
        return true;
    }

    private Relation() {}

    public Relation(Subject _subject) {
        related = _subject;
        relatedType = _subject.getRelatedType();
    }
}

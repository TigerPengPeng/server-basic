package developer.github.dao.model;

import lombok.Data;

import java.util.Date;

@Data
public class RelationDaoModel {
    private Long id;

    private Long createdById;

    private Date createdAt;

    private Long updatedById;

    private Date updatedAt;

    private Long version;

    private Integer status;

    private Long objectId;

    private Integer objectType;

    private String related;

    private Integer relatedType;
}


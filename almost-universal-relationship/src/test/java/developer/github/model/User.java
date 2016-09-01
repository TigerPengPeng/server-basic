package developer.github.model;

import developer.github.service.annotation.RelatedField;
import developer.github.service.annotation.RelatedObject;
import developer.github.service.model.ObjectType;
import developer.github.service.model.RelatedType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @package: developer.github.model
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午4:27
 * @description:
 */
@RelatedObject(objectType = ObjectType.USER)
@Data
public class User {
    private Long id;

    private String name;

    private Integer age;

    @RelatedField(relatedType = RelatedType.USER_SOCIAL_NETWORK_IDS, runtimeClass = ArrayList.class)
    private List<Long> socialNetworkIds;
}

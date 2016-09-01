package developer.github.service.model;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @class:
 * @author: 黄鹏
 * @description:
 * <br>
 *     使用在Annotation: @RelatedField上
 *     在根据带有Annotation: @RelatedField的field, 解析生成数据库数据
 *     在根据数据库数据, 将对应值set对object的对应field上
 *     需要注意的时候, type 值不能更改; name 值是field的名称, 需要跟随field更改而更改
 * </br>
 */
public enum RelatedType {

    USER_SOCIAL_NETWORK_IDS(10001, "socialNetworkIds");

    private final int type;

    private final String field;

    RelatedType(int _type, String _field) {
        type = _type;
        field = _field;
    }

    public static RelatedType getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (RelatedType relatedType : values()) {
            if (relatedType.getType() == type) {
                return relatedType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public static List<Integer> parseIntegerList(Collection<RelatedType> relatedTypes) {
        if (CollectionUtils.isEmpty(relatedTypes)) {
            return new ArrayList<>();
        }
        List<Integer> list = new ArrayList<>(relatedTypes.size());
        for (RelatedType relatedType : relatedTypes) {
            list.add(relatedType.getType());
        }
        return list;
    }
}

package developer.github.service.model;

/**
 * @class:
 * @author: 黄鹏
 * @description:
 * <br>
 *     使用在Annotation: @RelatedObject上, 用于获取该class 对象的object_type
 *     attention: 需要保证每个class的 object_type 唯一
 * </br>
 */
public enum ObjectType {

    USER(10000, "User.java");

    private final int type;

    private final String description;

    ObjectType(int _type, String _des) {
        type = _type;
        description = _des;
    }

    public static ObjectType getByType(Integer type) {
        if (type == null) {
            return null;
        }

        for (ObjectType objectType : values()) {
            if (objectType.getType() == type) {
                return objectType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

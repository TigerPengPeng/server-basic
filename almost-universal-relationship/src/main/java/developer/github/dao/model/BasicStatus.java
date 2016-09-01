package developer.github.dao.model;

/**
 * @package: developer.github.dao.model
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午3:51
 * @description:
 */
public enum BasicStatus {
    /**
     * 删除状态
     */
    DELETE(0, "删除"),

    /**
     * 存在状态
     */
    EXISTS(1, "存在");

    private final int type;

    private final String description;

    public static BasicStatus getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (BasicStatus basicStatus : values()) {
            if (basicStatus.getType() == type) {
                return basicStatus;
            }
        }
        return null;
    }

    BasicStatus(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}

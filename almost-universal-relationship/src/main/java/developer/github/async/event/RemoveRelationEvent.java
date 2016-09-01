package developer.github.async.event;

import lombok.Getter;

import java.io.Serializable;

/**
 * @package: developer.github.async.event
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午4:02
 * @description:
 */
public class RemoveRelationEvent implements Serializable {

    @Getter
    private Long objectId;

    private RemoveRelationEvent() {}

    public RemoveRelationEvent(Long _objectId) {
        objectId = _objectId;
    }
}

package developer.github.service;

import developer.github.model.UserMetaVO;

import java.util.Collection;
import java.util.Map;

/**
 * @package: developer.github.service
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月17日 下午12:03
 * @description:
 */
public interface UserMetaCacheService {
    /**
     * get user meta map caches by principalIds from redis
     * @param principalIds
     * @return
     */
    Map<String, UserMetaVO> getKeyValuePair(Collection<String> principalIds);
}

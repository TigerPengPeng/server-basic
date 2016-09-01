package developer.github.service.impl;

import developer.github.model.UserMetaVO;
import developer.github.service.UserMetaCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @package: developer.github.service
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月17日 下午12:02
 * @description:
 */
@Service("userMetaCacheService")
@Slf4j
public class UserMetaCacheServiceImpl implements UserMetaCacheService {
    /**
     * todo now it's test
     * get user meta map caches by principalIds from redis
     *
     * @param principalIds
     * @return
     */
    @Override
    public Map<String, UserMetaVO> getKeyValuePair(Collection<String> principalIds) {
        if (CollectionUtils.isEmpty(principalIds)) {
            return new HashMap<>();
        }

        Map<String, UserMetaVO> keyValuePair = new HashMap<>();
        for (String principalId : principalIds) {
            Long longValue = null;
            try {
                longValue = Long.parseLong(principalId);
            } catch (Throwable t) {
                log.error("{}", t);
            }
            if (longValue == null) {
                continue;
            }

            UserMetaVO userMetaVO = new UserMetaVO();
            userMetaVO.setId(longValue);
            userMetaVO.setName("name_" + longValue);

            keyValuePair.put(principalId, userMetaVO);
        }

        return keyValuePair;
    }
}

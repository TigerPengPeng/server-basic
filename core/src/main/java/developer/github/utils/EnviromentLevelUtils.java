package developer.github.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月30日 下午11:06
 * @description:
 */
@Slf4j
public class EnviromentLevelUtils {

    private static final String ONLINE_LEVEL_ARGS = "scale";

    private volatile static String onlineLevel;

    public static String getLevel() {
        if (onlineLevel == null) {
            synchronized (EnviromentLevelUtils.class) {
                if (onlineLevel == null) {
                    String value = JvmParameterUtils.get(ONLINE_LEVEL_ARGS);
                    if (StringUtils.isEmpty(value)) {
                        throw new IllegalArgumentException("jvm argument " + ONLINE_LEVEL_ARGS + " is null");
                    }

                    onlineLevel = value;
                }
            }
        }
        return onlineLevel;
    }
}

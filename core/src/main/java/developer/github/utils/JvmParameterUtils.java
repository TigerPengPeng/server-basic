package developer.github.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月30日 下午10:59
 * @description:
 */
@Slf4j
public class JvmParameterUtils {

    public static String get(String arg) {
        String value = System.getProperty(arg);
        log.info("Check JVM argument: -D{}={}", arg, value);
        return value;
    }
}

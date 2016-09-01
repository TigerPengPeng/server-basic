package developer.github.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @package: developer.github.annotation
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月16日 下午6:30
 * @description:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserMeta {

    String name();
}

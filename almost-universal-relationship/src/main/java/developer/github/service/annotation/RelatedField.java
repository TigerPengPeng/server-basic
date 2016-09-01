package developer.github.service.annotation;

import developer.github.service.model.RelatedType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @class:
 * @author: 黄鹏
 * @description:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RelatedField {

    RelatedType relatedType();

    Class<?> runtimeClass();
}

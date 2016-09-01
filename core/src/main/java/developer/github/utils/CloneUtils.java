package developer.github.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月21日 上午11:22
 * @description:
 */
@Slf4j
public class CloneUtils {

    /**
     * deep clone
     * @param object
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T clone(T object) {
        if (object == null) {
            return null;
        }

        T target = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            target = (T) ois.readObject();
        } catch (Throwable t){
            log.error("{}", t);
        }
        return target;
    }
}

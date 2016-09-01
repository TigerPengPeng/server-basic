package developer.github;

import developer.github.core.CoreContext;
import developer.github.factory.jackson.BindModuleFactory;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.JedisCluster;

/**
 * @package: developer.github
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月17日 下午1:52
 * @description:
 */
public class CoreApplicationTest {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/*-applicationContext.xml");
        JedisCluster jedisCluster = CoreContext.getBean("jedisCluster", JedisCluster.class);

        BindModuleFactory bindModuleFactory = CoreContext.getBean("bindModuleFactory", BindModuleFactory.class);
        System.out.println("test end");
    }
}

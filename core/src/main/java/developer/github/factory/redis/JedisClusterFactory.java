package developer.github.factory.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

/**
 * @package: developer.github.factory.redis
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月17日 下午1:36
 * @description:
 */
@Slf4j
public class JedisClusterFactory {

    private JedisCluster jedisCluster;

    public JedisClusterFactory(String hosts, Integer timeout, Integer maxRedirections) {
        log.info("connecting to redis cluster");
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        for (String host : hosts.split(";")) {
            jedisClusterNodes.add(new HostAndPort(host.split(":")[0], Integer.valueOf(host.split(":")[1])));
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxRedirections, poolConfig);
    }

    public JedisCluster get() {
        return jedisCluster;
    }

    @PreDestroy
    public void destroy() {
        try {
            jedisCluster.close();
        } catch (Throwable t) {
            log.error("{}", t);
        }
    }
}

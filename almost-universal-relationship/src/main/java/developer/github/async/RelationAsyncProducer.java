package developer.github.async;

import developer.github.producer.TopicAsyncProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午4:06
 * @description:
 */
@Component("relationAsyncProducer")
@Slf4j
public class RelationAsyncProducer extends TopicAsyncProducer implements AsyncProducer {
    /**
     * exchange:
     * topic: defaultRoutingKey
     * direct: queue name
     *
     * @return
     */
    @Override
    protected String getDefaultRoutingKey() {
        return "relation.async";
    }
}

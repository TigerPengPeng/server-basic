package developer.github.async;

import developer.github.consumer.TopicAsyncConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月12日 下午4:08
 * @description:
 */
@Component("relationAsyncConsumer")
@Slf4j
public class RelationAsyncConsumer extends TopicAsyncConsumer implements AsyncConsumer {
    @Override
    public String getQueueName() {
        return "async.relation";
    }

    @Override
    public List<String> getBindingKeys() {
        return Arrays.asList("relation.*");
    }
}

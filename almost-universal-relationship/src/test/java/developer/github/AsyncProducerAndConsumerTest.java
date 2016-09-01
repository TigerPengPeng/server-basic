package developer.github;

import developer.github.model.User;
import developer.github.service.RelationService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AsyncProducerAndConsumerTest {

	@Test
	public void testSendMessageAndReceive() throws Throwable {
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("classpath*:spring/*-applicationContext.xml");
		RelationService relationService = context.getBean("relationService", RelationService.class);

		User user = new User();
		user.setId(65535L);
		List<Long> socialNetworkIds = Arrays.asList(1000L, 2000L, 3000L, 4000L, 5000L);
		user.setSocialNetworkIds(socialNetworkIds);
		relationService.save(user);

		Thread.sleep(2000);

		User parseUser = new User();
		parseUser.setId(65535L);
		parseUser = relationService.parseObjectRelations(parseUser);
		System.out.println(parseUser);
		assertArrayEquals(socialNetworkIds.toArray(), parseUser.getSocialNetworkIds().toArray());
	}
}

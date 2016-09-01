package developer.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import developer.github.core.CoreContext;
import developer.github.factory.jackson.BindModuleFactory;
import developer.github.model.UserMetaModel;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserMetaSerializerTest {

	@Test
	public void testSendMessageAndReceive() throws Throwable {
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("classpath*:spring/*-applicationContext.xml");

		BindModuleFactory bindModuleFactory = CoreContext.getBean("bindModuleFactory", BindModuleFactory.class);
		ObjectMapper useMetaObjectMapper = CoreContext.getBean("userMetaObjectMapper", ObjectMapper.class);
		ObjectMapper objectMapper = CoreContext.getBean("objectMapper", ObjectMapper.class);

		UserMetaModel userMetaModel = UserMetaModel.getTestMetaModel();
		String normalValue = objectMapper.writeValueAsString(userMetaModel);
		String userMetaValue = useMetaObjectMapper.writeValueAsString(userMetaModel);

		System.out.println(normalValue);
		System.out.println(userMetaValue);
	}
}

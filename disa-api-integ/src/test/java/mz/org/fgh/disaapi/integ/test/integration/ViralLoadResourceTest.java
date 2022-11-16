package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;
import mz.org.fgh.disaapi.integ.test.fixturefactory.ViralLoadTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ViralLoadResourceTest {

	private static boolean dataLoaded = false;

	@Inject
	private ViralLoadService viralLoadService;

	@Autowired
	private TestRestTemplate restTemplate;

	private UserContext userContext;

	private ViralLoad vl;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("mz.org.fgh.disaapi.integ.test.fixturefactory");
	}

	@Before
	public void before() throws BusinessException {
		userContext = new UserContext();
		userContext.setUuid(UuidFactory.generate());
		userContext.setUsername("hisfgh");

		vl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.VALID);

		if (dataLoaded) {
			return;
		}
		viralLoadService.createViralLoad(userContext, vl);
		dataLoaded = true;
	}

	@Test
	public void deleteShouldReturnOk() {
		Map<String, String> uriVariable = Collections.singletonMap("requestId", vl.getRequestId());
		ResponseEntity<String> response = restTemplate.withBasicAuth("disa", "disa")
				.exchange("/viralloads/{requestId}", HttpMethod.DELETE, null, String.class,
						uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void deleteShouldReturnNotFound() {
		Map<String, String> uriVariable = Collections.singletonMap("requestId", "NON EXISTING REQUESTID");
		ResponseEntity<String> response = restTemplate.withBasicAuth("disa", "disa")
				.exchange("/viralloads/{requestId}", HttpMethod.DELETE, null, String.class,
						uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}

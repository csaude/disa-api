package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;
import mz.org.fgh.disaapi.integ.test.fixturefactory.ViralLoadTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ViralLoadResourceTest {

	private static final String RESULT_URL = "/viralloads/{requestId}";

	private static boolean dataLoaded = false;

	@Inject
	private ViralLoadService viralLoadService;

	@Inject
	private TestRestTemplate restTemplate;

	private UserContext userContext;

	private ViralLoad vl;

	private ViralLoad notProcessedVl;

	private ViralLoad inactiveVl;

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
		notProcessedVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);
		inactiveVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.INACTIVE);

		if (dataLoaded) {
			return;
		}
		viralLoadService.createViralLoad(userContext, vl);
		viralLoadService.createViralLoad(userContext, notProcessedVl);

		// GenericDAO wont let us create entities with INACTIVE status.
		// So we need create and update the status.
		viralLoadService.createViralLoad(userContext, inactiveVl);
		inactiveVl.inactive();
		viralLoadService.updateViralLoad(userContext, inactiveVl);

		dataLoaded = true;
	}

	@Test
	public void deleteShouldReturnOk() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, String> uriVariable = Collections.singletonMap("requestId", vl.getRequestId());
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void deleteShouldReturnNotFound() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, String> uriVariable = Collections.singletonMap("requestId", "NON EXISTING REQUESTID");
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void updateShouldUpdateTheViralLoad() throws JSONException {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		JSONObject vlJson = new JSONObject();
		vlJson.put("viralLoadStatus", "PENDING");
		HttpEntity<String> pendingVlEntity = new HttpEntity<String>(vlJson.toString(), headers);

		Map<String, String> uriVariable = Collections.singletonMap("requestId", notProcessedVl.getRequestId());

		ResponseEntity<ViralLoad> response = restTemplate.exchange(RESULT_URL, HttpMethod.PATCH, pendingVlEntity,
				ViralLoad.class, uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getViralLoadStatus()).isEqualTo(ViralLoadStatus.PENDING);
	}

	@Test
	public void updateShouldReturnNotFound() throws JSONException {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		JSONObject vlJson = new JSONObject();
		vlJson.put("viralLoadStatus", "PENDING");
		HttpEntity<String> pendingVlEntity = new HttpEntity<String>(vlJson.toString(), headers);

		Map<String, String> uriVariable = Collections.singletonMap("requestId", inactiveVl.getRequestId());

		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.PATCH, pendingVlEntity,
				String.class, uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + performBasicAuthEncoding("disa", "disa"));
		return httpHeaders;
	}

	private String performBasicAuthEncoding(String user, String password) {
		String combined = user + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		return new String(encodedBytes);
	}
}

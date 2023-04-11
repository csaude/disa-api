package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViralLoadResourceV2_2Test {

	private static final String SEARCH_URL = "/v2.1.2/viralloads/search?"
			+ "startDate={startDate}&"
			+ "endDate={endDate}&"
			+ "healthFacilityLabCode={healthFacilityLabCode}&"
			+ "requestId={requestId}&"
			+ "referringRequestID={referringRequestID}&"
			+ "viralLoadStatus={viralLoadStatus}&"
			+ "notProcessingCause={notProcessingCause}&"
			+ "nid={nid}&"
			+ "pageNumber={pageNumber}&";

	@Inject
	private ViralLoadService viralLoadService;

	@Inject
	private TestRestTemplate restTemplate;

	private UserContext userContext;

	private List<ViralLoad> vls;

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

		vls = EntityFactory.gimme(ViralLoad.class, 15, ViralLoadTemplate.VALID);
		notProcessedVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);
		inactiveVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.INACTIVE);

		for (ViralLoad vl : vls) {
			viralLoadService.createViralLoad(userContext, vl);
		}

		viralLoadService.createViralLoad(userContext, notProcessedVl);

		// GenericDAO wont let us create entities with INACTIVE status.
		// So we need create and update the status.
		viralLoadService.createViralLoad(userContext, inactiveVl);
		inactiveVl.inactive();
		viralLoadService.updateViralLoad(userContext, inactiveVl);
	}

	@Test
	public void findViralLoadsByFormShouldReturnPaginatedResults() throws JSONException {
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("startDate", "2022-01-01 00:00:00");
		uriVariables.put("endDate", tomorrowMidnight.format(ofPattern));
		uriVariables.put("healthFacilityLabCode", "01041137");
		uriVariables.put("requestId", null);
		uriVariables.put("nid", null);
		uriVariables.put("referringRequestID", null);
		uriVariables.put("viralLoadStatus", null);
		uriVariables.put("notProcessingCause", null);
		uriVariables.put("pageNumber", null);

		// Get first page
		ResponseEntity<String> response1 = restTemplate
				.exchange(SEARCH_URL, HttpMethod.GET, httpEntity, String.class, uriVariables);
		String page1 = response1.getBody();


		assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response1.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		String expected = "{\"content\": [{},{},{},{},{},{},{},{},{},{}], \"number\":0, \"size\":10, \"numberOfElements\": 10}";
		JSONAssert.assertEquals(expected, page1, false);

		// Get second page
		uriVariables.put("pageNumber", "1");
		ResponseEntity<String> response2 = restTemplate.exchange(SEARCH_URL, HttpMethod.GET, httpEntity,
				String.class, uriVariables);

		String page2 = response2.getBody();
		String expected2 = "{\"content\": [{},{},{},{},{},{}], \"number\":1, \"size\":10, \"numberOfElements\": 6}";
		JSONAssert.assertEquals(expected2, page2, false);
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

package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.org.fgh.disaapi.core.viralload.model.Page;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;
import mz.org.fgh.disaapi.integ.test.fixturefactory.ViralLoadTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViralLoadResourceTest {

	private static final String RESULT_URL = "/v2/viralloads/{requestId}";

	private static final String SEARCH_URL = "/v2/viralloads/search-form?"
			+ "startDate={startDate}&"
			+ "endDate={endDate}&"
			+ "healthFacilityLabCode={healthFacilityLabCode}&"
			+ "requestId={requestId}&"
			+ "referringRequestID={referringRequestID}&"
			+ "viralLoadStatus={viralLoadStatus}&"
			+ "notProcessingCause={notProcessingCause}&"
			+ "nid={nid}&"
			+ "pageNumber={pageNumber}&";

	private static final String EXPORT_URL = "/v2/viralloads/export?"
			+ "startDate={startDate}&"
			+ "endDate={endDate}&"
			+ "healthFacilityLabCode={healthFacilityLabCode}&"
			+ "requestId={requestId}&"
			+ "referringRequestID={referringRequestID}&"
			+ "viralLoadStatus={viralLoadStatus}&"
			+ "notProcessingCause={notProcessingCause}&"
			+ "nid={nid}&";

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

		vls = EntityFactory.gimme(ViralLoad.class, 15, ViralLoadTemplate.VALID);
		notProcessedVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);
		inactiveVl = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.INACTIVE);

		for (ViralLoad vl : vls) {
			viralLoadService.createViralLoad(vl);
		}

		viralLoadService.createViralLoad(notProcessedVl);

		// GenericDAO wont let us create entities with INACTIVE status.
		// So we need create and update the status.
		viralLoadService.createViralLoad(inactiveVl);
		inactiveVl.inactive();
		viralLoadService.updateViralLoad(inactiveVl);
	}

	@Test
	@WithMockUser
	public void deleteShouldReturnOk() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, String> uriVariable = Collections.singletonMap("requestId", vls.get(0).getRequestId());
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@WithMockUser
	public void deleteShouldReturnNotFound() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, String> uriVariable = Collections.singletonMap("requestId", "NON EXISTING REQUESTID");
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@WithMockUser
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
	@WithMockUser
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

	@Test
	@WithMockUser
	public void findViralLoadsByFormShouldReturnPaginatedResults() {
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
		uriVariables.put("pageNumber", "1");

		ParameterizedTypeReference<Page<ViralLoad>> responseType = new ParameterizedTypeReference<Page<ViralLoad>>() {
		};

		// Get first page
		ResponseEntity<Page<ViralLoad>> response1 = restTemplate
				.exchange(SEARCH_URL, HttpMethod.GET, httpEntity, responseType, uriVariables);
		Page<ViralLoad> page1 = response1.getBody();
		assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response1.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		assertThat(page1.getPageNumber()).isEqualTo(1);
		assertThat(page1.getPageSize()).isEqualTo(ViralLoadQueryService.DEFAULT_PAGE_SIZE);
		assertThat(page1.getTotalResults()).isEqualTo(16);
		assertThat(page1.getResultList()).hasSize(ViralLoadQueryService.DEFAULT_PAGE_SIZE);

		// Get second page
		uriVariables.put("pageNumber", "2");
		ResponseEntity<Page<ViralLoad>> response2 = restTemplate.exchange(SEARCH_URL, HttpMethod.GET, httpEntity,
				responseType, uriVariables);

		Page<ViralLoad> page2 = response2.getBody();
		assertThat(page2.getPageNumber()).isEqualTo(2);
		assertThat(page2.getPageSize()).isEqualTo(ViralLoadQueryService.DEFAULT_PAGE_SIZE);
		assertThat(page2.getTotalResults()).isEqualTo(16);
		assertThat(page2.getResultList()).hasSize(16 - ViralLoadQueryService.DEFAULT_PAGE_SIZE);
	}

	@Test
	@WithMockUser
	public void exportViralLoadsByFormShouldReturnAllResults() {
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

		ParameterizedTypeReference<List<ViralLoad>> responseType = new ParameterizedTypeReference<List<ViralLoad>>() {
		};

		ResponseEntity<List<ViralLoad>> response = restTemplate
				.exchange(EXPORT_URL, HttpMethod.GET, httpEntity, responseType, uriVariables);
		List<ViralLoad> viralLoads = response.getBody();
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		assertThat(viralLoads).hasSize(16);
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

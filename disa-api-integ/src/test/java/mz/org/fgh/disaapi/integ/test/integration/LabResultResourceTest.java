package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.integ.DisaApiIntegApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DisaApiIntegApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:LabResultResourceTest.sql")
public class LabResultResourceTest {

	private static final String RESULT_URL = "/lab-results/{id}";

	private static final String SEARCH_URL = "/lab-results/search";

	private static final String EXPORT_URL = "/lab-results/export";

	@Inject
	protected TestRestTemplate restTemplate;

	protected List<LabResult> vls;

	private LabResult notProcessedVl;

	private LabResult inactiveVl;

	protected LabResult fromUnauthorizedOrgUnit;

	protected ImplementingPartner fgh;

	@Before
	public void setUp() {

		vls = LongStream.range(1l, 16l)
				.mapToObj(l -> {
					HIVVLLabResult hivvlLabResult = new HIVVLLabResult();
					hivvlLabResult.setId(l);
					return hivvlLabResult;
				})
				.collect(Collectors.toList());

		List<String> codes = Arrays.asList("1040111", "1040107", "1040106", "1040114");
		Set<OrgUnit> orgUnits = codes.stream()
				.map(code -> {
					OrgUnit ou = new OrgUnit();
					ou.setCode(code);
					return ou;
				})
				.collect(Collectors.toSet());
		fgh = new ImplementingPartner();
		fgh.setOrgUnits(orgUnits);

		inactiveVl = new HIVVLLabResult();
		inactiveVl.setId(17l);

		fromUnauthorizedOrgUnit = new HIVVLLabResult();
		fromUnauthorizedOrgUnit.setId(18l);
	}

	@Test
	public void deleteShouldReturnOk() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, Long> uriVariable = Collections.singletonMap("id", vls.get(0).getId());
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void deleteShouldReturnNotFound() {
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, Long> uriVariable = Collections.singletonMap("id", 0l);
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void findViralLoadsByFormShouldReturnPaginatedResults() {
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
		uriVariables.add("startDate", "2022-01-01 00:00:00");
		uriVariables.add("endDate", tomorrowMidnight.format(ofPattern));
		uriVariables.addAll("healthFacilityLabCode", new ArrayList<>(fgh.getOrgUnitCodes()));
		uriVariables.add("requestId", null);
		uriVariables.add("nid", null);
		uriVariables.add("referringRequestID", null);
		uriVariables.add("labResultStatus", null);
		uriVariables.add("notProcessingCause", null);
		uriVariables.add("pageNumber", null);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SEARCH_URL).queryParams(uriVariables);

		// Get first page
		ResponseEntity<String> response1 = restTemplate
				.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
		String page1 = response1.getBody();

		assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response1.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		String expected = "{\"content\": [{},{},{},{},{},{},{},{},{},{}], \"number\":0, \"size\":10, \"numberOfElements\": 10}";
		JSONAssertEquals(expected, page1, false);

		// Get second page
		builder.replaceQueryParam("pageNumber", "1");
		ResponseEntity<String> response2 = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity,
				String.class);

		String page2 = response2.getBody();
		String expected2 = "{\"content\": [{},{},{},{},{},{}], \"number\":1, \"size\":10, \"numberOfElements\": 6}";
		JSONAssertEquals(expected2, page2, false);
	}

	@Test
	public void deleteShouldNotProceedForViralLoadsFromUnauthorizedOrgUnit() {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		Map<String, Long> uriVariable = Collections.singletonMap("id", fromUnauthorizedOrgUnit.getId());
		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.DELETE, httpEntity, String.class,
				uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void updateShouldUpdateTheViralLoad() throws JSONException {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		JSONObject vlJson = new JSONObject();
		vlJson.put("labResultStatus", "PENDING");
		HttpEntity<String> pendingVlEntity = new HttpEntity<String>(vlJson.toString(), headers);

		Map<String, Long> uriVariable = Collections.singletonMap("id", 16l);

		ResponseEntity<HIVVLLabResult> response = restTemplate.exchange(RESULT_URL, HttpMethod.PATCH, pendingVlEntity,
				HIVVLLabResult.class, uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getLabResultStatus()).isEqualTo(LabResultStatus.PENDING);
	}

	@Test
	public void updateShouldNotProceedForViralLoadsFromUnauthorizedOrgUnit() throws JSONException {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		JSONObject vlJson = new JSONObject();
		vlJson.put("labResultStatus", "PENDING");
		HttpEntity<String> pendingVlEntity = new HttpEntity<String>(vlJson.toString(), headers);

		Map<String, Long> uriVariable = Collections.singletonMap("id", 18l);

		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.PATCH, pendingVlEntity,
				String.class, uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void updateShouldReturnNotFound() throws JSONException {

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		JSONObject vlJson = new JSONObject();
		vlJson.put("labResultStatus", "PENDING");
		HttpEntity<String> pendingVlEntity = new HttpEntity<String>(vlJson.toString(), headers);

		Map<String, Long> uriVariable = Collections.singletonMap("id", inactiveVl.getId());

		ResponseEntity<String> response = restTemplate.exchange(RESULT_URL, HttpMethod.PATCH, pendingVlEntity,
				String.class, uriVariable);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void findViralLoadsByFormShouldFailIfGivenUnauthorizedOrgUnit() {
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

		ArrayList<String> orgUnitCodes = new ArrayList<>(fgh.getOrgUnitCodes());
		orgUnitCodes.add(fromUnauthorizedOrgUnit.getHealthFacilityLabCode());

		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
		uriVariables.add("startDate", "2022-01-01 00:00:00");
		uriVariables.add("endDate", tomorrowMidnight.format(ofPattern));
		uriVariables.addAll("healthFacilityLabCode", orgUnitCodes);
		uriVariables.add("requestId", null);
		uriVariables.add("nid", null);
		uriVariables.add("referringRequestID", null);
		uriVariables.add("labResultStatus", null);
		uriVariables.add("notProcessingCause", null);
		uriVariables.add("pageNumber", "1");

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SEARCH_URL).queryParams(uriVariables);

		// Get first page
		ResponseEntity<String> response1 = restTemplate
				.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
		assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	public void exportViralLoadsByFormShouldReturnAllResults() {
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
		uriVariables.add("startDate", "2022-01-01 00:00:00");
		uriVariables.add("endDate", tomorrowMidnight.format(ofPattern));
		uriVariables.addAll("healthFacilityLabCode", new ArrayList<>(fgh.getOrgUnitCodes()));
		uriVariables.add("requestId", null);
		uriVariables.add("nid", null);
		uriVariables.add("referringRequestID", null);
		uriVariables.add("labResultStatus", null);
		uriVariables.add("notProcessingCause", null);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(EXPORT_URL).queryParams(uriVariables);

		ParameterizedTypeReference<List<HIVVLLabResult>> responseType = new ParameterizedTypeReference<List<HIVVLLabResult>>() {
		};

		ResponseEntity<List<HIVVLLabResult>> response = restTemplate
				.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, responseType);
		List<HIVVLLabResult> viralLoads = response.getBody();
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		assertThat(viralLoads).hasSize(16);
	}

	@Test
	public void exportViralLoadsByFormShouldFailIfGivenUnauthorizedOrgUnit() {
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

		ArrayList<String> orgUnitCodes = new ArrayList<>(fgh.getOrgUnitCodes());
		orgUnitCodes.add(fromUnauthorizedOrgUnit.getHealthFacilityLabCode());

		MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
		uriVariables.add("startDate", "2022-01-01 00:00:00");
		uriVariables.add("endDate", tomorrowMidnight.format(ofPattern));
		uriVariables.addAll("healthFacilityLabCode", orgUnitCodes);
		uriVariables.add("requestId", null);
		uriVariables.add("nid", null);
		uriVariables.add("referringRequestID", null);
		uriVariables.add("labResultStatus", null);
		uriVariables.add("notProcessingCause", null);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(EXPORT_URL).queryParams(uriVariables);

		ResponseEntity<String> response = restTemplate
				.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + performBasicAuthEncoding("fgh", "fgh"));
		return httpHeaders;
	}

	private String performBasicAuthEncoding(String user, String password) {
		String combined = user + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		return new String(encodedBytes);
	}

	private void JSONAssertEquals(String expected, String actual, boolean strict) {
		try {
			JSONAssert.assertEquals(expected, actual, strict);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}

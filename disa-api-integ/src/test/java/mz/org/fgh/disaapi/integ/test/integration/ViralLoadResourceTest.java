package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.processor.HibernateProcessor;
import mz.co.fgh.disaapi.core.fixturefactory.ImplementingPartnerTemplate;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.Page;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration
public class ViralLoadResourceTest {

	private static final int VL_COUNT = 15;

	private static final String SEARCH_URL = "/v2/viralloads/search-form?"
			+ "startDate={startDate}&"
			+ "endDate={endDate}&"
			+ "healthFacilityLabCode={healthFacilityLabCode}&"
			+ "requestId={requestId}&"
			+ "referringRequestID={referringRequestID}&"
			+ "labResultStatus={labResultStatus}&"
			+ "notProcessingCause={notProcessingCause}&"
			+ "nid={nid}&"
			+ "pageNumber={pageNumber}&";

	@Inject
	protected TestRestTemplate restTemplate;

	@Inject
	private EntityManagerFactory emFactory;

	protected List<LabResult> vls;

	protected LabResult fromUnauthorizedOrgUnit;

	protected ImplementingPartner fgh;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
	}

	@Before
	public void before() throws BusinessException {

		SessionFactory sessionFactory = emFactory.unwrap(SessionFactory.class);
		try (Session session = sessionFactory.openSession()) {

			Transaction tx = session.beginTransaction();

			HibernateProcessor hibernateProcessor = new HibernateProcessor(session);

			vls = Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(VL_COUNT, ViralLoadTemplate.VALID);

			fromUnauthorizedOrgUnit = Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(ViralLoadTemplate.MAPUTO);

			fgh = Fixture.from(ImplementingPartner.class)
					.uses(hibernateProcessor)
					.gimme(ImplementingPartnerTemplate.FGH);

			for (OrgUnit orgUnit : fgh.getOrgUnits()) {
				orgUnit.setImplementingPartnerId(fgh.getId());
				session.save(orgUnit);
			}

			tx.commit();
		}
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
		uriVariables.add("pageNumber", "1");

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SEARCH_URL).queryParams(uriVariables);

		ParameterizedTypeReference<Page<HIVVLLabResult>> responseType = new ParameterizedTypeReference<Page<HIVVLLabResult>>() {
		};

		// Get first page
		ResponseEntity<Page<HIVVLLabResult>> response1 = restTemplate
				.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, responseType);
		Page<HIVVLLabResult> page1 = response1.getBody();
		assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response1.getHeaders().get("Content-Type").get(0)).isEqualTo("application/json");
		assertThat(page1.getPageNumber()).isEqualTo(1);
		assertThat(page1.getPageSize()).isEqualTo(LabResultQueryService.DEFAULT_PAGE_SIZE);
		assertThat(page1.getTotalResults()).isEqualTo(VL_COUNT);
		assertThat(page1.getResultList()).hasSize(LabResultQueryService.DEFAULT_PAGE_SIZE);

		// Get second page
		builder.replaceQueryParam("pageNumber", "2");
		ResponseEntity<Page<HIVVLLabResult>> response2 = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
				httpEntity,
				responseType);

		Page<HIVVLLabResult> page2 = response2.getBody();
		assertThat(page2.getPageNumber()).isEqualTo(2);
		assertThat(page2.getPageSize()).isEqualTo(LabResultQueryService.DEFAULT_PAGE_SIZE);
		assertThat(page2.getTotalResults()).isEqualTo(VL_COUNT);
		assertThat(page2.getResultList()).hasSize(VL_COUNT - LabResultQueryService.DEFAULT_PAGE_SIZE);
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
}

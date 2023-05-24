/**
 *
 */
package mz.co.fgh.disaapi.core.viralload.integ;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.processor.HibernateProcessor;
import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.OrgUnitTemplate;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;

/**
 * @author Stélio Moiane
 *
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class LabResultQueryServiceTest extends AbstractIntegServiceTest {

	@Inject
	protected LabResultQueryService labResultQueryService;

	@Inject
	private EntityManagerFactory emFactory;

	private List<LabResult> viralLoads;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
	}

	@Before
	public void before() {

		SessionFactory sessionFactory = emFactory.unwrap(SessionFactory.class);
		try (Session session = sessionFactory.openSession()) {

			Transaction tx = session.beginTransaction();

			HibernateProcessor hibernateProcessor = new HibernateProcessor(session);

			viralLoads = Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(10, ViralLoadTemplate.VALID);

			Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(5, ViralLoadTemplate.MAPUTO);

			Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(2, ViralLoadTemplate.NOT_PROCESSED);

			Fixture.from(HIVVLLabResult.class)
					.uses(hibernateProcessor)
					.gimme(ViralLoadTemplate.PROCESSED);

			tx.commit();
		}

	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnFromSpecificProvince() throws BusinessException {

		final List<LabResult> viralLoads = this.labResultQueryService
				.findPendingByLocationCodeAndProvince(Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
						"Zambezia");

		assertThat(viralLoads).hasSize(10);
	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnPendingResultsFromGivenLabCodes() throws BusinessException {

		String[] allCodes = Arrays.copyOf(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES,
				OrgUnitTemplate.ZAMBEZIA_SISMA_CODES.length + 1);
		allCodes[allCodes.length - 1] = "1100100";

		final List<LabResult> viralLoads = this.labResultQueryService
				.findPendingByLocationCode(Arrays.asList(allCodes));

		assertThat(viralLoads).hasSize(15);
	}

	@Test
	public void findAllByFormShouldReturnResultsByExampleVlLocationCodeAndBetweenDates() throws BusinessException {

		LocalDate today = LocalDate.now();
		LocalDateTime startDate = today.atStartOfDay();
		LocalDateTime endDate = today.atTime(LocalTime.MAX);
		LabResult example = new HIVVLLabResult();
		example.active();
		List<LabResult> findAllByForm = this.labResultQueryService.findAllByForm(example,
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES), startDate, endDate);
		assertThat(findAllByForm).hasSize(13);
	}

	@Test
	public void findViralLoadByNidShouldReturnResultsWithGivensNids() throws BusinessException {

		List<String> nids = viralLoads.stream()
				.limit(3)
				.map(vl -> vl.getNid())
				.collect(Collectors.toList());
		List<LabResult> findAllByForm = this.labResultQueryService.findViralLoadByNid(nids);
		assertThat(findAllByForm).hasSize(3);
	}

	@Test
	public void findViralLoadByRequestIdShouldReturnResultsWithGivenRequestIds() throws BusinessException {

		List<String> requestIds = viralLoads.stream()
				.limit(2)
				.map(vl -> vl.getRequestId())
				.collect(Collectors.toList());
		List<LabResult> findAllByForm = this.labResultQueryService.findViralLoadByRequestId(requestIds);
		assertThat(findAllByForm).hasSize(2);
	}

	@Test
	public void findByStatusShouldReturnResultsFromGivenHealthFacilitiesAndStatus() throws BusinessException {
		List<LabResult> findByStatus = labResultQueryService.findByLocaationCodeAndStatus(
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
				LabResultStatus.NOT_PROCESSED);
		assertThat(findByStatus).hasSize(2);
	}

	@Test
	public void findByStatusAndDatesShouldReturnResultsFromGivenHealthFacilitiesAndStatusBetweenDates()
			throws BusinessException {
		LocalDate today = LocalDate.now();
		LocalDateTime startDate = today.atStartOfDay();
		LocalDateTime endDate = today.atTime(LocalTime.MAX);
		List<LabResult> findByStatusAndDates = labResultQueryService.findByLocationCodeAndStatusBetweenDates(
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
				LabResultStatus.PROCESSED, startDate,
				endDate);
		assertThat(findByStatusAndDates).hasSize(1);
	}
}

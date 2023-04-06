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
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;

/**
 * @author Stélio Moiane
 *
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViralLoadQueryServiceTest extends AbstractIntegServiceTest {

	@Inject
	protected ViralLoadQueryService viralLoadQueryService;

	@Inject
	private EntityManagerFactory emFactory;

	private List<ViralLoad> viralLoads;

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

			viralLoads = Fixture.from(ViralLoad.class)
					.uses(hibernateProcessor)
					.gimme(10, ViralLoadTemplate.VALID);

			Fixture.from(ViralLoad.class)
					.uses(hibernateProcessor)
					.gimme(5, ViralLoadTemplate.MAPUTO);

			Fixture.from(ViralLoad.class)
					.uses(hibernateProcessor)
					.gimme(2, ViralLoadTemplate.NOT_PROCESSED);

			Fixture.from(ViralLoad.class)
					.uses(hibernateProcessor)
					.gimme(ViralLoadTemplate.PROCESSED);

			tx.commit();
		}

	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnFromSpecificProvince() throws BusinessException {

		final List<ViralLoad> viralLoads = this.viralLoadQueryService
				.findPendingByLocationCodeAndProvince(Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
						"Zambezia");

		assertThat(viralLoads).hasSize(10);
	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnPendingResultsFromGivenLabCodes() throws BusinessException {

		String[] allCodes = Arrays.copyOf(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES,
				OrgUnitTemplate.ZAMBEZIA_SISMA_CODES.length + 1);
		allCodes[allCodes.length - 1] = "1100100";

		final List<ViralLoad> viralLoads = this.viralLoadQueryService
				.findPendingByLocationCode(Arrays.asList(allCodes));

		assertThat(viralLoads).hasSize(15);
	}

	@Test
	public void findAllByFormShouldReturnResultsByExampleVlLocationCodeAndBetweenDates() throws BusinessException {

		LocalDate today = LocalDate.now();
		LocalDateTime startDate = today.atStartOfDay();
		LocalDateTime endDate = today.atTime(LocalTime.MAX);
		ViralLoad example = new ViralLoad();
		example.active();
		List<ViralLoad> findAllByForm = this.viralLoadQueryService.findAllByForm(example,
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES), startDate, endDate);
		assertThat(findAllByForm).hasSize(13);
	}

	@Test
	public void findViralLoadByNidShouldReturnResultsWithGivensNids() throws BusinessException {

		List<String> nids = viralLoads.stream()
				.limit(3)
				.map(vl -> vl.getNid())
				.collect(Collectors.toList());
		List<ViralLoad> findAllByForm = this.viralLoadQueryService.findViralLoadByNid(nids);
		assertThat(findAllByForm).hasSize(3);
	}

	@Test
	public void findViralLoadByRequestIdShouldReturnResultsWithGivenRequestIds() throws BusinessException {

		List<String> requestIds = viralLoads.stream()
				.limit(2)
				.map(vl -> vl.getRequestId())
				.collect(Collectors.toList());
		List<ViralLoad> findAllByForm = this.viralLoadQueryService.findViralLoadByRequestId(requestIds);
		assertThat(findAllByForm).hasSize(2);
	}

	@Test
	public void findByStatusShouldReturnResultsFromGivenHealthFacilitiesAndStatus() throws BusinessException {
		List<ViralLoad> findByStatus = viralLoadQueryService.findByLocaationCodeAndStatus(
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
				ViralLoadStatus.NOT_PROCESSED);
		assertThat(findByStatus).hasSize(2);
	}

	@Test
	public void findByStatusAndDatesShouldReturnResultsFromGivenHealthFacilitiesAndStatusBetweenDates()
			throws BusinessException {
		LocalDate today = LocalDate.now();
		LocalDateTime startDate = today.atStartOfDay();
		LocalDateTime endDate = today.atTime(LocalTime.MAX);
		List<ViralLoad> findByStatusAndDates = viralLoadQueryService.findByLocationCodeAndStatusBetweenDates(
				Arrays.asList(OrgUnitTemplate.ZAMBEZIA_SISMA_CODES),
				ViralLoadStatus.PROCESSED, startDate,
				endDate);
		assertThat(findByStatusAndDates).hasSize(1);
	}
}

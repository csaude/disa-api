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

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

/**
 * @author Stélio Moiane
 *
 */
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViralLoadQueryServiceTest extends AbstractIntegServiceTest {

	@Inject
	protected ViralLoadQueryService viralLoadQueryService;

	@Inject
	protected ViralLoadService viralLoadService;

	private List<ViralLoad> viralLoads;

	@Before
	public void before() throws BusinessException {

		viralLoads = EntityFactory.gimme(ViralLoad.class, 10, ViralLoadTemplate.VALID);

		List<ViralLoad> fromMaputo = EntityFactory.gimme(ViralLoad.class, 5, ViralLoadTemplate.MAPUTO);
		viralLoads.addAll(fromMaputo);

		List<ViralLoad> notProcessed = EntityFactory.gimme(ViralLoad.class, 2, ViralLoadTemplate.NOT_PROCESSED);
		viralLoads.addAll(notProcessed);

		ViralLoad processed = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.PROCESSED);
		viralLoads.add(processed);

		viralLoads.forEach(viralLoad -> {
			createViralLoad(viralLoad);
		});

	}

	private ViralLoad createViralLoad(final ViralLoad viralLoad) {
		try {
			this.viralLoadService.createViralLoad(this.getUserContext(), viralLoad);
		} catch (final BusinessException e) {
			e.printStackTrace();
		}
		return viralLoad;
	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnFromSpecificProvince() throws BusinessException {

		final List<ViralLoad> viralLoads = this.viralLoadQueryService
				.findPendingByLocationCodeAndProvince(Arrays.asList("01041137", "01041137"), "Zambezia");

		assertThat(viralLoads).hasSize(10);
	}

	@Test
	public void findByLocationCodeAndStatusShouldReturnPendingResultsFromGivenLabCodes() throws BusinessException {

		final List<ViralLoad> viralLoads = this.viralLoadQueryService
				.findPendingByLocationCode(Arrays.asList("01041137", "1100100"));

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
				Arrays.asList("01041137"), startDate, endDate);
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
		List<ViralLoad> findByStatus = viralLoadQueryService.findByLocaationCodeAndStatus(Arrays.asList("01041137"),
				ViralLoadStatus.NOT_PROCESSED);
		assertThat(findByStatus).hasSize(2);
	}

	@Test
	public void findByStatusAndDatesShouldReturnResultsFromGivenHealthFacilitiesAndStatusBetweenDates()
			throws BusinessException {
		LocalDate today = LocalDate.now();
		LocalDateTime startDate = today.atStartOfDay();
		LocalDateTime endDate = today.atTime(LocalTime.MAX);
		List<ViralLoad> findByStatusAndDates = viralLoadQueryService.findByLocationCodeAndStatusBetweenDates(Arrays.asList("01041137"),
				ViralLoadStatus.PROCESSED, startDate,
				endDate);
		assertThat(findByStatusAndDates).hasSize(1);
	}
}

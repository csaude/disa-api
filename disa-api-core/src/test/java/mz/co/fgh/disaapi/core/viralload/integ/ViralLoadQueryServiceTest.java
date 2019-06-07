/**
 *
 */
package mz.co.fgh.disaapi.core.viralload.integ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

/**
 * @author Stélio Moiane
 *
 */
public class ViralLoadQueryServiceTest extends AbstractIntegServiceTest {

	@Inject
	private ViralLoadQueryService viralLoadQueryService;

	@Inject
	private ViralLoadService viralLoadService;

	private ViralLoad viralLoad;

	private List<String> nids;

	private List<String> locationCodes;

	@Before
	public void before() throws BusinessException {

		this.viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.VALID);

		final List<ViralLoad> viralLoads = EntityFactory.gimme(ViralLoad.class, 10, ViralLoadTemplate.VALID);
		nids = new ArrayList<String>();
		locationCodes = new ArrayList<String>();

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
	public void shouldFindByLocationCodeAndStatus() throws BusinessException {

		locationCodes.addAll(Arrays.asList("01041137", "01041137"));

		final List<ViralLoad> viralLoads = this.viralLoadQueryService.findByLocationCodeAndStatus(locationCodes);

		assertFalse(viralLoads.isEmpty());
	}

	@Test
	public void shouldFindViralLoadByNid() throws BusinessException {

		nids.addAll(Arrays.asList("0091A", "01041137"));

		viralLoad.setNid("0091A");

		viralLoadService.createViralLoad(getUserContext(), viralLoad);

		assertTrue(!this.viralLoadQueryService.findViralLoadByNid(nids).isEmpty());
	}
}

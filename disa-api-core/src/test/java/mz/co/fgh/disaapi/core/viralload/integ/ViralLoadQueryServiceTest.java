/**
 *
 */
package mz.co.fgh.disaapi.core.viralload.integ;

import static org.junit.Assert.assertFalse;

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

	@Before
	public void before() throws BusinessException {
		final List<ViralLoad> viralLoads = EntityFactory.gimme(ViralLoad.class, 10, ViralLoadTemplate.VALID);

		viralLoads.forEach(viralLoad -> {
			this.createViralLoad(viralLoad);
		});
	}

	private void createViralLoad(final ViralLoad viralLoad) {
		try {
			this.viralLoadService.createViralLoad(this.getUserContext(), viralLoad);
		}
		catch (final BusinessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldFindAllViralLoad() throws BusinessException {

		final List<ViralLoad> viralLoads = this.viralLoadQueryService.findAllViralLoad();

		assertFalse(viralLoads.isEmpty());
	}
}

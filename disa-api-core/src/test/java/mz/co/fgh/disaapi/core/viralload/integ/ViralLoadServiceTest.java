package mz.co.fgh.disaapi.core.viralload.integ;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

public class ViralLoadServiceTest extends AbstractIntegServiceTest {

	@Inject
	private ViralLoadQueryService viralLoadQueryService;

	@Inject
	private ViralLoadService viralLoadService;

	private ViralLoad viralLoad;
	private List<String> nids;

	public ViralLoadServiceTest() throws BusinessException {
		this.viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.VALID);
		nids = new ArrayList<String>();

	}

	@Test
	@WithMockUser
	public void shouldCreateViralLoad() throws BusinessException {

		this.viralLoadService.createViralLoad(viralLoad);

		assertFalse(viralLoad.getId() == null);

	}

	@Test
	@WithMockUser
	public void shouldUpdateViralLoad() throws BusinessException {

		viralLoadService.createViralLoad(viralLoad);

		nids.addAll(Arrays.asList("01041137", "01041137"));

		this.viralLoadQueryService.findViralLoadByNid(nids);

		viralLoad.setViralLoadStatus(ViralLoadStatus.NOT_PROCESSED);

		viralLoadService.updateViralLoad(viralLoad);

		assertFalse(viralLoad.getViralLoadStatus() == ViralLoadStatus.PENDING);

	}
}

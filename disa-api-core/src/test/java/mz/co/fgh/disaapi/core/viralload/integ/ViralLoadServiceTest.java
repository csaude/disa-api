package mz.co.fgh.disaapi.core.viralload.integ;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

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
	protected ViralLoadQueryService viralLoadQueryService;

	@Inject
	protected ViralLoadService viralLoadService;

	private ViralLoad viralLoad;
	private List<String> nids;

	public ViralLoadServiceTest() {
		this.viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.VALID);
		nids = new ArrayList<String>();
	}

	@Test
	public void shouldCreateViralLoad() throws BusinessException {

		this.viralLoadService.createViralLoad(this.getUserContext(), viralLoad);

		assertThat(viralLoad.getId()).isNotNull();

	}

	@Test
	public void shouldUpdateViralLoad() throws BusinessException {

		viralLoadService.createViralLoad(getUserContext(), viralLoad);

		nids.addAll(Arrays.asList("01041137", "01041137"));

		this.viralLoadQueryService.findViralLoadByNid(nids);

		viralLoad.setViralLoadStatus(ViralLoadStatus.NOT_PROCESSED);

		viralLoadService.updateViralLoad(getUserContext(), viralLoad);

		assertThat(viralLoad.getViralLoadStatus()).isNotEqualTo(ViralLoadStatus.PENDING);

	}
}

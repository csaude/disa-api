package mz.co.fgh.disaapi.core.viralload.integ;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import jakarta.persistence.EntityManagerFactory;
import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.service.LabResultService;

@ContextConfiguration
public class LabResultServiceTest extends AbstractIntegServiceTest {

	@Inject
	private EntityManagerFactory emFactory;

	@Inject
	protected LabResultService labResultService;

	private LabResult viralLoad;

	@BeforeClass
	public static void setUp() throws SQLException {
		FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
		// Server.createWebServer("-web", "-webAllowOthers", "-webPort",
		// "8082").start();
	}

	@Before
	public void before() {
		SessionFactory sessionFactory = emFactory.unwrap(SessionFactory.class);
		try (Session session = sessionFactory.openSession()) {

			Transaction tx = session.beginTransaction();

			viralLoad = Fixture.from(HIVVLLabResult.class)
					.gimme(ViralLoadTemplate.VALID);

			viralLoad = session.merge(viralLoad);

			tx.commit();
		}
	}

	@Test
	@WithMockUser
	public void shouldUpdateLabResult() throws BusinessException {

		assertThat(viralLoad.getLabResultStatus()).isEqualTo(LabResultStatus.PENDING);

		viralLoad.setLabResultStatus(LabResultStatus.NOT_PROCESSED);

		labResultService.updateLabResult(viralLoad);

		assertThat(viralLoad.getLabResultStatus()).isEqualTo(LabResultStatus.NOT_PROCESSED);

	}

	@Test
	@WithMockUser
	public void updateDuplicateRequestIdShouldInactivate() throws BusinessException {
		viralLoad.setLabResultStatus(LabResultStatus.NOT_PROCESSED);
		viralLoad.setNotProcessingCause(NotProcessingCause.DUPLICATED_REQUEST_ID);

		labResultService.updateLabResult(viralLoad);

		assertThat(viralLoad.getEntityStatus()).isEqualTo(EntityStatus.INACTIVE);

	}
}

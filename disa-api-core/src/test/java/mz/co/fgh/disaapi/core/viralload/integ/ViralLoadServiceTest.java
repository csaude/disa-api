package mz.co.fgh.disaapi.core.viralload.integ;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

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
import br.com.six2six.fixturefactory.processor.HibernateProcessor;
import mz.co.fgh.disaapi.core.config.AbstractIntegServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

@ContextConfiguration
public class ViralLoadServiceTest extends AbstractIntegServiceTest {

	@Inject
	private EntityManagerFactory emFactory;

	@Inject
	protected ViralLoadService viralLoadService;

	private ViralLoad viralLoad;


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

			viralLoad = Fixture.from(ViralLoad.class)
					.uses(hibernateProcessor)
					.gimme(ViralLoadTemplate.VALID);

			tx.commit();
		}
	}

	@Test
	@WithMockUser
	public void shouldUpdateViralLoad() throws BusinessException {

		assertThat(viralLoad.getViralLoadStatus()).isEqualTo(ViralLoadStatus.PENDING);

		viralLoad.setViralLoadStatus(ViralLoadStatus.NOT_PROCESSED);

		viralLoadService.updateViralLoad(viralLoad);

		assertThat(viralLoad.getViralLoadStatus()).isEqualTo(ViralLoadStatus.NOT_PROCESSED);

	}
}

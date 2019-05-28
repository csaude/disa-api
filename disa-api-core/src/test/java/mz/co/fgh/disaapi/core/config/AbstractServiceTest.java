/**
 *
 */
package mz.co.fgh.disaapi.core.config;

import org.junit.BeforeClass;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.UuidFactory;

/**
 * @author Stélio Moiane
 *
 */
public abstract class AbstractServiceTest {

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
	}

	public UserContext getUserContext() {
		final UserContext context = new UserContext();

		context.setUuid(UuidFactory.generate());
		context.setUsername("hisfgh");

		return context;
	}
}

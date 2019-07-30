/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.config;

import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.UuidFactory;

/**
 * @author Stélio Moiane
 *
 */
public abstract class AbstractUserContext {

	public UserContext getUserContext() {
		final UserContext context = new UserContext();

		context.setUuid(UuidFactory.generate());
		context.setUsername("hisfgh");

		return context;
	}
}

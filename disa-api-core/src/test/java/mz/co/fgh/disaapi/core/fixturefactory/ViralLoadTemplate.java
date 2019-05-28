/**
 *
 */
package mz.co.fgh.disaapi.core.fixturefactory;

import java.time.LocalDate;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
public class ViralLoadTemplate implements TemplateLoader {

	public static final String VALID = "VALID";

	@Override
	public void load() {

		Fixture.of(ViralLoad.class).addTemplate(VALID, new Rule() {
			{
				this.add("nid", "ART.331/15");
				this.add("dateOfBirth", LocalDate.now());
			}
		});

	}
}

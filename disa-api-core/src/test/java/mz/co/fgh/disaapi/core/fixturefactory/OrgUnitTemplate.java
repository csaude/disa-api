package mz.co.fgh.disaapi.core.fixturefactory;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public class OrgUnitTemplate implements TemplateLoader {

    public static final String[] ZAMBEZIA_SISMA_CODES = new String[] { "1040106", "1040107", "1040111", "1040114" };

    static final String ZAMBEZIA = "ZAMBEZIA";

    static final String QUELIMANE = "QUELIMANE";

    @Override
    public void load() {
        Fixture.of(OrgUnit.class).addTemplate(ZAMBEZIA, new Rule() {
            {
                this.add("province", "Zambézia");
            }
        });

        Fixture.of(OrgUnit.class).addTemplate(QUELIMANE).inherits(ZAMBEZIA, new Rule() {
            {
                this.add("district", "Quelimane");
                this.add("facility",
                        uniqueRandom("17 de Setembro CSURB", "Namuinho CS III", "Chabeco CSURB", "24 de Julho CSURB"));
                this.add("code", uniqueRandom(ZAMBEZIA_SISMA_CODES));
            }
        });

    }

}

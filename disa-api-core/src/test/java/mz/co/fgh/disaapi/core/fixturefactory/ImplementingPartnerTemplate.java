package mz.co.fgh.disaapi.core.fixturefactory;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public class ImplementingPartnerTemplate implements TemplateLoader {

    private static final String ACTIVE = "ACTIVE";
    public static final String FGH = "FGH";

    @Override
    public void load() {
        Fixture.of(ImplementingPartner.class).addTemplate(ACTIVE, new Rule() {
            {
                this.add("enabled", true);
            }
        });

        Fixture.of(ImplementingPartner.class).addTemplate(FGH).inherits(ACTIVE, new Rule() {
            {
                this.add("orgName", "fgh");
                this.add("password", "$2a$10$41nSgDPAlGNFqMwaUnBVl.NgKxz14ipAcj/JluJbipbammDr9BKXu"); // fgh
                this.add("orgUnits", has(4).of(OrgUnit.class, OrgUnitTemplate.QUELIMANE));
            }
        });
    }

}

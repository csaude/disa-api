/**
 *
 */
package mz.co.fgh.disaapi.core.fixturefactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
public class ViralLoadTemplate implements TemplateLoader {

	public static final String INACTIVE = "INACTIVE";

	public static final String VALID = "VALID";

	public static final String NOT_PROCESSED = "NOT_PROCESSED";

	public static final String PROCESSED = "PROCESSED";

	public static final String MAPUTO = "Maputo";

	@Override
	public void load() {

		Fixture.of(ViralLoad.class).addTemplate(VALID, new Rule() {
			{
				this.add("nid", regex("0001041137/\\d{4}/\\d{5}"));
				this.add("requestId", regex("MZDISAPQM\\d{7}"));
				this.add("dateOfBirth", LocalDate.now());
				this.add("healthFacilityLabCode", uniqueRandom("1040106", "1040107", "1040111", "1040114"));
				this.add("nameOfTechnicianRequestingTest", "ITALIDA");
				this.add("requestingFacilityName", uniqueRandom("17 de Setembro CSURB", "Namuinho CS III", "Chabeco CSURB", "24 de Julho CSURB"));
				this.add("requestingProvinceName", "Zambezia");
				this.add("encounter", "SMI");
				this.add("pregnant", "Unreported");
				this.add("breastFeeding", "Yes");
				this.add("reasonForTest", "Routine");
				this.add("harvestDate", LocalDateTime.now());
				this.add("harvestType", "Unreported");
				this.add("dateOfSampleReceive", LocalDateTime.now());
				this.add("rejectedReason", "N/A");
				this.add("processingDate", LocalDateTime.now());
				this.add("sampleType", "Dry Blood Spot");
				this.add("viralLoadResultCopies", "1000");
				this.add("viralLoadResultLog", "133");
				this.add("viralLoadResultQualitative", "Suppressed");
				this.add("viralLoadResultDate", LocalDateTime.now());
				this.add("aprovedBy", "ITALIDA");
				this.add("labComments", "N/A");
				this.add("viralLoadStatus", ViralLoadStatus.PENDING);
				this.add("createdAt", LocalDateTime.now());
				this.add("createdBy", "fgh");
				this.add("entityStatus", EntityStatus.ACTIVE);
				this.add("uuid", UUID.randomUUID().toString());

			}
		});

		Fixture.of(ViralLoad.class).addTemplate(NOT_PROCESSED).inherits(VALID, new Rule() {
			{
				this.add("viralLoadStatus", ViralLoadStatus.NOT_PROCESSED);
			}
		});

		Fixture.of(ViralLoad.class).addTemplate(PROCESSED).inherits(VALID, new Rule() {
			{
				this.add("viralLoadStatus", ViralLoadStatus.PROCESSED);
			}
		});

		Fixture.of(ViralLoad.class).addTemplate(INACTIVE).inherits(VALID, new Rule() {
			{
				this.add("entityStatus", EntityStatus.INACTIVE);
			}
		});

		Fixture.of(ViralLoad.class).addTemplate(MAPUTO).inherits(VALID, new Rule() {
			{
				this.add("nid", "000${healthFacilityLabCode}/0000/00000");
				this.add("healthFacilityLabCode", "1100100");
				this.add("requestingProvinceName", "Maputo");
			}
		});
	}
}

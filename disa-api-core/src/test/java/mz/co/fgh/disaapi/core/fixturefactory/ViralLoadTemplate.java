/**
 *
 */
package mz.co.fgh.disaapi.core.fixturefactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
public class ViralLoadTemplate implements TemplateLoader {

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
				this.add("healthFacilityLabCode", "01041137");
				this.add("nameOfTechnicianRequestingTest", "ITALIDA");
				this.add("requestingFacilityName", "CS 16 de Junho");
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

		Fixture.of(ViralLoad.class).addTemplate(MAPUTO).inherits(VALID, new Rule() {
			{
				this.add("nid", "000${healthFacilityLabCode}/0000/00000");
				this.add("healthFacilityLabCode", "1100100");
				this.add("requestingProvinceName", "Maputo");
			}
		});
	}
}

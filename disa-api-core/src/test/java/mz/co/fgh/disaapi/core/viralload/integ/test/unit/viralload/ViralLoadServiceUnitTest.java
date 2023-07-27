package mz.co.fgh.disaapi.core.viralload.integ.test.unit.viralload;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import mz.co.fgh.disaapi.core.config.AbstractUnitServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;
import mz.org.fgh.disaapi.core.result.service.LabResultService;

@ContextConfiguration
public class ViralLoadServiceUnitTest extends AbstractUnitServiceTest {

    @Inject
    private LabResultService viralLoadService;

    @MockBean
    private LabResultRepository viralLoadRepository;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
    }

    @Test
    @WithMockUser
    public void updateLabResultShouldUpdateViralLoadPropertyValues() throws BusinessException {

        LabResult viralLoad = EntityFactory.gimme(HIVVLLabResult.class, ViralLoadTemplate.NOT_PROCESSED);

        Mockito.when(viralLoadRepository.findByIdAndEntityStatus(viralLoad.getId(), EntityStatus.ACTIVE))
                .thenReturn(viralLoad);

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("labResultStatus", "PENDING");
        propertyValues.put("healthFacilityLabCode", "1040107");
        propertyValues.put("requestingFacilityName", "CS 24 de Julho");
        this.viralLoadService.updateLabResult(
                viralLoad,
                propertyValues);

        assertThat(viralLoad.getLabResultStatus()).isEqualTo(LabResultStatus.PENDING);
        assertThat(viralLoad.getHealthFacilityLabCode()).isEqualTo("1040107");

    }

    @Test
    @WithMockUser
    public void updateLabResultShouldUpdateAllowedViralLoadPropertyValues() throws BusinessException {

        LabResult viralLoad = EntityFactory.gimme(HIVVLLabResult.class, ViralLoadTemplate.NOT_PROCESSED);

        Mockito.when(viralLoadRepository.findByIdAndEntityStatus(viralLoad.getId(), EntityStatus.ACTIVE))
                .thenReturn(viralLoad);

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("reasonForTest", "Suspected treatment failure");
        this.viralLoadService.updateLabResult(
                viralLoad,
                propertyValues);

        assertThat(viralLoad.getReasonForTest()).isEqualTo("Routine");

    }
}

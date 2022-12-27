package mz.co.fgh.disaapi.core.viralload.integ.test.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import mz.co.fgh.disaapi.core.config.AbstractUnitServiceTest;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.fixturefactory.EntityFactory;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

public class ViralLoadServiceUnitTest extends AbstractUnitServiceTest {

    @Inject
    private ViralLoadService viralLoadService;

    @MockBean
    private ViralLoadDAO viralLoadDAO;

    @Test
    public void updateViralLoadShouldUpdateViralLoadPropertyValues() throws BusinessException {

        ViralLoad viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);

        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        Mockito.when(viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE))
                .thenReturn(Arrays.asList(viralLoad));

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("viralLoadStatus", "PENDING");
        propertyValues.put("healthFacilityLabCode", "1040107");
        propertyValues.put("requestingFacilityName", "CS 24 de Julho");
        this.viralLoadService.updateViralLoad(
                getUserContext(),
                viralLoad,
                propertyValues);

        assertThat(viralLoad.getViralLoadStatus()).isEqualTo(ViralLoadStatus.PENDING);
        assertThat(viralLoad.getHealthFacilityLabCode()).isEqualTo("1040107");

    }

    @Test
    public void updateViralLoadShouldUpdateAllowedViralLoadPropertyValues() throws BusinessException {

        ViralLoad viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);

        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        Mockito.when(viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE))
                .thenReturn(Arrays.asList(viralLoad));

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("reasonForTest", "Suspected treatment failure");
        this.viralLoadService.updateViralLoad(
                getUserContext(),
                viralLoad,
                propertyValues);

        assertThat(viralLoad.getReasonForTest()).isEqualTo("Routine");

    }

    @Test(expected = BusinessException.class)
    public void updateViralLoadShouldFailWhenViralLoadProcessed() throws BusinessException {

        ViralLoad viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.PROCESSED);

        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        Mockito.when(viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE))
                .thenReturn(Arrays.asList(viralLoad));

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("viralLoadStatus", "PENDING");
        propertyValues.put("healthFacilityLabCode", "1040107");
        propertyValues.put("requestingFacilityName", "CS 24 de Julho");
        this.viralLoadService.updateViralLoad(
                getUserContext(),
                viralLoad,
                propertyValues);

    }

    @Test(expected = BusinessException.class)
    public void updateViralLoadShouldFailWhenPropertyEmpty() throws BusinessException {

        ViralLoad viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.NOT_PROCESSED);

        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        Mockito.when(viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE))
                .thenReturn(Arrays.asList(viralLoad));

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("viralLoadStatus", "");
        this.viralLoadService.updateViralLoad(
                getUserContext(),
                viralLoad,
                propertyValues);
    }

    @Test(expected = NotFoundBusinessException.class)
    public void updateViralLoadShouldFailWhenViralNotFound() throws BusinessException {

        ViralLoad viralLoad = EntityFactory.gimme(ViralLoad.class, ViralLoadTemplate.PROCESSED);

        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        Mockito.when(viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE))
                .thenReturn(Collections.emptyList());

        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put("viralLoadStatus", "PENDING");
        propertyValues.put("healthFacilityLabCode", "1040107");
        propertyValues.put("requestingFacilityName", "CS 24 de Julho");
        this.viralLoadService.updateViralLoad(
                getUserContext(),
                viralLoad,
                propertyValues);

    }
}

package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.repository.ViralLoadRepository;

@Service
public class ViralLoadServiceImpl implements ViralLoadService {

    @Inject
    ViralLoadRepository viralLoadRepository;

    @Override
    public ViralLoad createViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException {
        viralLoad.setCreatedBy(context.getUuid());
        viralLoad.setCreatedAt(LocalDateTime.now());
        viralLoad.active();

        if (viralLoad.getUuid() == null) {
            viralLoad.setUuid(UuidFactory.generate());
        }

        return viralLoadRepository.save(viralLoad);
    }

    @Override
    public ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException {
        viralLoad.setUpdatedBy(context.getUuid());
        viralLoad.setUpdatedAt(LocalDateTime.now());

        return viralLoadRepository.save(viralLoad);
    }

    @Override
    public ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad, Map<String, Object> propertyValues)
            throws BusinessException {
        List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
        List<ViralLoad> vls = viralLoadRepository.findByRequestIdInAndEntityStatus(requestIds, EntityStatus.ACTIVE);

        if (vls.isEmpty()) {
            throw new NotFoundBusinessException(
                    "Viral load " + viralLoad.getRequestId() + " was not found.");
        }

        ViralLoad dbVl = vls.get(0);

        validateStatus(viralLoad, dbVl);

        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(dbVl);
        for (Entry<String, Object> entry : propertyValues.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (getAllowedPropertiesForUpdate().contains(key)) {
                if (ObjectUtils.isEmpty(value)) {
                    throw new BusinessException(key + " cannot be empty.");
                }
                bw.setPropertyValue(key, value);
            }
        }

        return this.updateViralLoad(context, (ViralLoad) bw.getWrappedInstance());
    }

    @Override
    public List<String> getAllowedPropertiesForUpdate() {
        return Arrays.asList(
                "viralLoadStatus",
                "requestingProvinceName",
                "requestingDistrictName",
                "requestingFacilityName",
                "healthFacilityLabCode");
    }

    private void validateStatus(ViralLoad viralLoad, ViralLoad dbVl) throws BusinessException {
		if (ViralLoadStatus.PROCESSED == dbVl.getViralLoadStatus()) {
			throw new BusinessException(
					"Cannot reschedule viral load " + viralLoad.getRequestId() + ". It has already been processed.");
		}
	}
}

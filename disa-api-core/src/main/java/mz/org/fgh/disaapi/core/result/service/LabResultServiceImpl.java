package mz.org.fgh.disaapi.core.result.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;

@Service
public class LabResultServiceImpl implements LabResultService {

    @Inject
    LabResultRepository viralLoadRepository;

    @Override
    public LabResult updateLabResult(LabResult labResult) throws BusinessException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        labResult.setUpdatedBy(user.getUsername());
        labResult.setUpdatedAt(LocalDateTime.now());
        if (LabResultStatus.NOT_PROCESSED == labResult.getLabResultStatus()
                && NotProcessingCause.DUPLICATED_REQUEST_ID == labResult.getNotProcessingCause()) {
            labResult.inactive();
        }
        return viralLoadRepository.save(labResult);
    }

    @Override
    public LabResult updateLabResult(LabResult labResult, Map<String, Object> propertyValues)
            throws BusinessException {
        List<String> requestIds = Arrays.asList(labResult.getRequestId());
        List<LabResult> results = viralLoadRepository.findByRequestIdInAndEntityStatus(requestIds, EntityStatus.ACTIVE);

        if (results.isEmpty()) {
            throw new NotFoundBusinessException(
                    "Viral load " + labResult.getRequestId() + " was not found.");
        }

        LabResult dbResult = results.get(0);

        validateStatus(labResult, dbResult);

        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(dbResult);
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

        return this.updateLabResult((LabResult) bw.getWrappedInstance());
    }

    @Override
    public List<String> getAllowedPropertiesForUpdate() {
        return Arrays.asList(
                "labResultStatus",
                "notProcessingCause",
                "requestingProvinceName",
                "requestingDistrictName",
                "requestingFacilityName",
                "healthFacilityLabCode",
                "synchronizedBy");
    }

    private void validateStatus(LabResult labResult, LabResult dbVl) throws BusinessException {
        if (LabResultStatus.PROCESSED == dbVl.getLabResultStatus()) {
            throw new BusinessException(
                    "Cannot reschedule viral load " + labResult.getRequestId() + ". It has already been processed.");
        }
    }
}

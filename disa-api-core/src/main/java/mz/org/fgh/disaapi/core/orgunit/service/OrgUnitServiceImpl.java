package mz.org.fgh.disaapi.core.orgunit.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import mz.org.fgh.disaapi.core.orgunit.OrgUnitRepository;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

@Service
public class OrgUnitServiceImpl implements OrgUnitService {

    @Inject
    private OrgUnitRepository orgUnitRepository;

    @Override
    public List<OrgUnit> search(String term) {
        return orgUnitRepository.search(term);
    }

    @Override
    public OrgUnit findByCode(String code) {
        return orgUnitRepository.findByCode(code);
    }
}

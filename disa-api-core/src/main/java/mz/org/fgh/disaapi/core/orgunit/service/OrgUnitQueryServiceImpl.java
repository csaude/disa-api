package mz.org.fgh.disaapi.core.orgunit.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import mz.org.fgh.disaapi.core.orgunit.dao.OrgUnitDAO;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

@Service
public class OrgUnitQueryServiceImpl implements OrgUnitQueryService {

    @Inject
    private OrgUnitDAO orgUnitDAO;

    @Override
    public List<OrgUnit> search(String term) {
        return orgUnitDAO.search(term);
    }

    @Override
    public OrgUnit findByCode(String code) {
        return orgUnitDAO.findByCode(code);
    }
}

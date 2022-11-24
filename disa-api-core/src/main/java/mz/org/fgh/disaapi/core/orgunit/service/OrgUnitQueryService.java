package mz.org.fgh.disaapi.core.orgunit.service;

import java.util.List;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public interface OrgUnitQueryService {

    List<OrgUnit> search(String term);

    OrgUnit findByCode(String requestId);
}

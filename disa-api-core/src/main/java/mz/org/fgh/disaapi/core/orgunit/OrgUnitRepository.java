package mz.org.fgh.disaapi.core.orgunit;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, String> {

    Set<OrgUnit> findByCodeIn(List<String> codes);

}

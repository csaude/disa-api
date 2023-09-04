package mz.org.fgh.disaapi.core.orgunit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, String> {

    @Query(value="SELECT code, province, district, facility, implementingPartnerId, MATCH(province, district, facility) AGAINST (CONCAT(':term', '*') IN BOOLEAN MODE) AS score FROM OrgUnit ORDER BY score DESC", nativeQuery=true)
    List<OrgUnit> search(String term);

    OrgUnit findByCode(String code);

}

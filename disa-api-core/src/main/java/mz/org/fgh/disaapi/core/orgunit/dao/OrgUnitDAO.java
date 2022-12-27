package mz.org.fgh.disaapi.core.orgunit.dao;

import java.util.List;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

public interface OrgUnitDAO {
    class QUERY {
        public static final String search = "SELECT code, province, district, facility, MATCH(province, district, facility) AGAINST (CONCAT(:term, '*') IN BOOLEAN MODE) AS score "
                                          + "FROM OrgUnit "
                                          + "ORDER BY score DESC";
        public static final String findByCode = "from OrgUnit where code=:code";
    }

    class QUERY_NAME {
        public static final String search = "OrgUnit.search";
        public static final String findByCode = "OrgUnit.findByCode";
    }

    List<OrgUnit> search(String term);

    OrgUnit findByCode(String code);
}

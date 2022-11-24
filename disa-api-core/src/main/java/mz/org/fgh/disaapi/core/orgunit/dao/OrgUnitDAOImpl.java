package mz.org.fgh.disaapi.core.orgunit.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

@Repository
public class OrgUnitDAOImpl implements OrgUnitDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrgUnit> search(String term) {
        TypedQuery<OrgUnit> query = this.entityManager.createNamedQuery(OrgUnitDAO.QUERY_NAME.search, OrgUnit.class);
        query.setParameter("term", term);
        return query.getResultList();
    }

    @Override
    public OrgUnit findByCode(String code) {
        TypedQuery<OrgUnit> query = this.entityManager.createNamedQuery(OrgUnitDAO.QUERY_NAME.findByCode,
                OrgUnit.class);
        query.setParameter("code", code);
        return query.getSingleResult();
    }

}

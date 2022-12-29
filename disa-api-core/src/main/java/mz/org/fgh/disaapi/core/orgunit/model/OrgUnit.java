package mz.org.fgh.disaapi.core.orgunit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import mz.org.fgh.disaapi.core.orgunit.dao.OrgUnitDAO;

@NamedNativeQueries({
        @NamedNativeQuery(name = OrgUnitDAO.QUERY_NAME.search, query = OrgUnitDAO.QUERY.search, resultClass = OrgUnit.class) })
@NamedQueries({
        @NamedQuery(name = OrgUnitDAO.QUERY_NAME.findByCode, query = OrgUnitDAO.QUERY.findByCode) })
@Entity
public class OrgUnit {

    @Id
    @Column
    private String code;

    @Column
    private String province;

    @Column
    private String district;

    @Column
    private String facility;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
}
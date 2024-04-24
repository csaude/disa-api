package mz.org.fgh.disaapi.core.ip;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;

@Entity
@NamedEntityGraph(name = "graph.ImplementingPartner.orgUnits", attributeNodes = @NamedAttributeNode(value = "orgUnits"))
public class ImplementingPartner implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orgName;
    private String password;
    private boolean enabled;
    private String mailList;

    /**
     * Stores the HFs a partner is authorized to access.
     */
    @OneToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name = "ImplementingPartnerId")
    private Set<OrgUnit> orgUnits;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getOrgName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    public Set<String> getOrgUnitCodes() {
        return orgUnits.stream().map(OrgUnit::getCode).collect(Collectors.toSet());
    }

    public void setOrgUnits(Set<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public String getMailList() {
        return mailList;
    }

    public void setMailList(String mailList) {
        this.mailList = mailList;
    }

    public List<String> getMailListItems() {
        return Arrays.asList(mailList.split(","));
    }

}

package mz.org.fgh.disaapi.core.ip;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImplementingPartnerRepository extends JpaRepository<ImplementingPartner, Integer> {

    @EntityGraph(value = "graph.ImplementingPartner.orgUnits", type = EntityGraphType.LOAD)
    ImplementingPartner findByOrgName(String orgName);

}

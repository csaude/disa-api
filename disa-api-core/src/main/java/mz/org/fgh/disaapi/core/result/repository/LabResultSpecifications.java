package mz.org.fgh.disaapi.core.result.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import mz.org.fgh.disaapi.core.result.model.LabResult;

public class LabResultSpecifications {

    public static Specification<LabResult> createdInLocationBetweenDates(
            LabResult example,
            List<String> healthFacilityLabCodes,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        return (Specification<LabResult>) (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("entityStatus"), example.getEntityStatus()));
            predicates.add(root.get("healthFacilityLabCode").in(healthFacilityLabCodes));

            if (startDate != null) {
                predicates.add(builder.greaterThan(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(builder.lessThan(root.get("createdAt"), endDate));
            }

            if (!StringUtils.isEmpty(example.getRequestId())) {
                predicates.add(builder.equal(root.get("requestId"), example.getRequestId()));
            }

            if (!StringUtils.isEmpty(example.getReferringRequestID())) {
                predicates.add(builder.equal(root.get("referringRequestID"), example.getReferringRequestID()));
            }

            if (example.getLabResultStatus() != null) {
                predicates.add(builder.equal(root.get("labResultStatus"), example.getLabResultStatus()));
            }

            if (example.getNotProcessingCause() != null) {
                predicates.add(builder.equal(root.get("notProcessingCause"), example.getNotProcessingCause()));
            }

            if (example.getTypeOfResult() != null) {
                predicates.add(builder.equal(root.get("typeOfResult"), example.getTypeOfResult()));
            }

            if (!StringUtils.isEmpty(example.getNid())) {
                predicates.add(builder.equal(root.get("nid"), example.getNid()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

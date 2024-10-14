package com.tun.casestudy1.repository.specifications;

import com.tun.casestudy1.entity.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> getEmployeeSpecification(String searchValue, String filterType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchValue == null || searchValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            if ("all".equals(filterType)) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("phoneNumber"), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("level").as(String.class), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("salary").as(String.class), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("department").get("name"), "%" + searchValue + "%"),
                        criteriaBuilder.like(criteriaBuilder.function("DATE_FORMAT", String.class,
                                root.get("dOB"), criteriaBuilder.literal("%d/%m/%Y")), "%" + searchValue + "%")

                ));
            } else {
                switch (filterType) {
                    case "name" -> predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchValue + "%"));
                    case "dateOfBirth" -> predicates.add(criteriaBuilder.like(criteriaBuilder.function("DATE_FORMAT", String.class,
                            root.get("dOB"), criteriaBuilder.literal("%d/%m/%Y")), "%" + searchValue + "%"));
                    case "department" -> predicates.add(criteriaBuilder.like(root.get("department").get("name"), "%" + searchValue + "%"));
                    case "level" -> predicates.add(criteriaBuilder.like(root.get("level").as(String.class), "%" + searchValue + "%"));
                    case "phoneNumber" -> predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + searchValue + "%"));
                    case "salary" -> predicates.add(criteriaBuilder.like(root.get("salary").as(String.class), "%" + searchValue + "%"));
                    default -> {
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

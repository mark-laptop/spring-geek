package ru.ndg.shop.filter;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.ndg.shop.model.Customer;

import java.util.Map;

public class CustomerFilter {

    private Specification<Customer> specification;
    private Sort sort;
    private Map<String, String> params;

    public CustomerFilter(Map<String, String> params) {
        this.params = params;
        this.specification = Specification.where(null);
        this.sort = Sort.by(Sort.Direction.ASC, "lastName");
    }

    public Specification<Customer> getSpecification() {
        if (params == null) return specification;

        StringBuilder filtersOut = new StringBuilder("");

        processFilterByUser();
        processCountCustomer(filtersOut);
        processLastName(filtersOut);
        processFirstName(filtersOut);
        processEmail(filtersOut);
        processSort(filtersOut);

        params.put("filters_out", filtersOut.toString());

        return specification;
    }

    private void processSort(StringBuilder filtersOut) {
        String sortField = params.get("sort_field");
        String sortOrder = params.get("sort_order");
        if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty() ) {
            sort = Sort.by(Sort.Direction.valueOf(sortOrder), sortField);
            filtersOut.append("&sort_field=").append(sortField);
            filtersOut.append("&sort_order=").append(sortOrder);
        } else {
            sortField = "lastName";
            sortOrder = "ASC";
            sort = Sort.by(Sort.Direction.valueOf(sortOrder), sortField);
            filtersOut.append("&sort_field=").append(sortField);
            filtersOut.append("&sort_order=").append(sortOrder);
        }
    }

    public Sort getSort() {
        return sort;
    }

    private void processCountCustomer(StringBuilder filtersOut) {
        String countCustomers = params.get("count_customers");
        if (countCustomers != null) {
            filtersOut.append("&count_customers=").append(countCustomers);
        }
    }

    private void processLastName(StringBuilder filtersOut) {
        String lastNameParam = params.get("last_name");
        if (lastNameParam != null && !lastNameParam.isEmpty() && !Character.isWhitespace(lastNameParam.charAt(0))) {
            specification = specification.and(
                    (Specification<Customer>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("lastName"), String.format("%%%s%%", lastNameParam)));
            filtersOut.append("&last_name=").append(lastNameParam);
        }
    }

    private void processFirstName(StringBuilder filtersOut) {
        String firstNameParam = params.get("first_name");
        if (firstNameParam != null && !firstNameParam.isEmpty() && !Character.isWhitespace(firstNameParam.charAt(0))) {
            specification = specification.and(
                    (Specification<Customer>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("firstName"), String.format("%%%s%%", firstNameParam)));
            filtersOut.append("&first_name=").append(firstNameParam);
        }
    }

    private void processEmail(StringBuilder filtersOut) {
        String email = params.get("email");
        if (email != null && !email.isEmpty() && !Character.isWhitespace(email.charAt(0))) {
            specification = specification.and(
                    (Specification<Customer>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("email"), String.format("%%%s%%", email)));
            filtersOut.append("&email=").append(email);
        }
    }

    private void processFilterByUser() {
        String isAdminParam = params.get("isAdmin");
        String principalName = params.get("principalName");
        if (
                (isAdminParam != null && !isAdminParam.isEmpty()) && (principalName!= null && !principalName.isEmpty())
        ) {
            Boolean isAdmin = Boolean.valueOf(isAdminParam);
            if (!isAdmin) {
                specification = specification.and((Specification<Customer>) (root, query, builder) ->
                        builder.equal(root.get("user").get("username"), principalName));
            }
        }
    }
}

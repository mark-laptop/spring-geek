package ru.ndg.shop.filter;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.ndg.shop.model.Order;

import java.util.Map;

public class OrderFilter {

    private Specification<Order> specification;
    private Sort sort;
    private Map<String, String> params;

    public OrderFilter(Map<String, String> params) {
        this.specification = Specification.where(null);
        this.sort = Sort.by(Sort.Direction.ASC, "number");
        this.params = params;
    }

    public Specification<Order> getSpecification() {
        if (params == null) return specification;

        StringBuilder filtersOut = new StringBuilder("");

        processFilterByUser();
        processNumber(filtersOut);
        processRecipient(filtersOut);
        processAddress(filtersOut);

        params.put("filters_out", filtersOut.toString());
        return specification;
    }

    private void processRecipient(StringBuilder filtersOut) {
        String recipient = params.get("recipient");
        if (recipient != null && !recipient.isEmpty() && !Character.isWhitespace(recipient.charAt(0))) {
            specification = specification.and((Specification<Order>) (root, query, builder) ->
                    builder.like(root.get("recipient"), String.format("%%%s%%", recipient)));
            filtersOut.append("&recipient=").append(recipient);
        }
    }

    private void processAddress(StringBuilder filtersOut) {
        String address = params.get("address");
        if (address != null && !address.isEmpty() && !Character.isWhitespace(address.charAt(0))) {
            specification = specification.and((Specification<Order>) (root, query, builder) ->
                    builder.like(root.get("address"), String.format("%%%s%%", address)));
            filtersOut.append("&address=").append(address);
        }
    }

    private void processNumber(StringBuilder filtersOut) {
        String number = params.get("number");
        if (number != null && !number.isEmpty() && !Character.isWhitespace(number.charAt(0))) {
            specification = specification.and((Specification<Order>) (root, query, builder) ->
                    builder.like(root.get("number"), String.format("%%%s%%", number)));
            filtersOut.append("&number=").append(number);
        }
    }

    private void processFilterByUser() {
        String isAdminParam = params.get("isAdmin");
        try {
            boolean isAdmin = Boolean.parseBoolean(isAdminParam);
            if (!isAdmin) {
                String principalName = params.get("principalName");
                specification = specification.and((Specification<Order>) (root, query, builder) ->
                        builder.equal(root.get("customer").get("user").get("username"), principalName));
            }
        } catch (NumberFormatException ignore) {}
    }

    public Sort getSort() {
        return sort;
    }
}

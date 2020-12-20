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

        params.put("filters_out", filtersOut.toString());
        return specification;
    }

    private void processFilterByUser() {


    }

    public Sort getSort() {
        return sort;
    }
}

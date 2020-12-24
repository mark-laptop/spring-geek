package ru.ndg.shop.filter;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.ndg.shop.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public class ProductFilter {

    private Specification<Product> specification;
    private Sort sort;
    private Map<String, String> params;

    public ProductFilter(Map<String, String> params) {
        this.params = params;
        this.specification = Specification.where(null);
        this.sort = Sort.by(Sort.Direction.ASC, "name");
    }

    public Specification<Product> getSpecification() {
        if (params == null) return specification;

        StringBuilder filtersOut = new StringBuilder("");

        processCountProduct(filtersOut);
        processName(filtersOut);
        processMinPrice(filtersOut);
        processMaxPrice(filtersOut);
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
        }
    }

    public Sort getSort() {
        return this.sort;
    }

    private void processCountProduct(StringBuilder filtersOut) {
        String countProducts = params.get("count_products");
        if (countProducts != null) {
            filtersOut.append("&count_products=").append(countProducts);
        }
    }

    private void processName(StringBuilder filtersOut) {
        String nameParam = params.get("name");
        if (nameParam != null && !nameParam.isEmpty() && !Character.isWhitespace(nameParam.charAt(0))) {
            specification = specification.and(
                    (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("name"), String.format("%%%s%%", nameParam)));
            filtersOut.append("&name=").append(nameParam);
        }
    }

    private void processMinPrice(StringBuilder filtersOut) {
        String minPriceParam = params.get("min_price");
        if (minPriceParam != null && !minPriceParam.isEmpty() && !Character.isWhitespace(minPriceParam.charAt(0))) {
            try {
                double parseDouble = Double.parseDouble(minPriceParam);
                specification = specification.and(
                        (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(parseDouble)));
                filtersOut.append("&min_price=").append(minPriceParam);
            } catch (NumberFormatException ignore) {
            }
        }
    }

    private void processMaxPrice(StringBuilder filtersOut) {
        String maxPriceParam = params.get("max_price");
        if (maxPriceParam != null && !maxPriceParam.isEmpty() && !Character.isWhitespace(maxPriceParam.charAt(0))) {
            try {
                double parseDouble = Double.parseDouble(maxPriceParam);
                specification = specification.and(
                        (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                                criteriaBuilder.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(parseDouble)));
                filtersOut.append("&max_price=").append(maxPriceParam);
            } catch (NumberFormatException ignore) {
            }
        }
    }
}

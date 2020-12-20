package ru.ndg.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;

    private BigDecimal quantity;

    private BigDecimal price;

    private BigDecimal sum;

    private OrderDto order;

    private ProductDto product;
}

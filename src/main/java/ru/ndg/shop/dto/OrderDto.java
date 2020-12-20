package ru.ndg.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private String number;

    private LocalDateTime createDate;

    private String recipient;

    private String address;

    private BigDecimal quantity;

    private BigDecimal sum;

    private CustomerDto customer;

    private Set<OrderItemDto> orderItems = new HashSet<>();
}

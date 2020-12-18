package ru.ndg.shop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products", indexes = {@Index(name = "IX_product_name", columnList = "name")})
@NoArgsConstructor
@Getter
@ToString
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    @Setter
    private String name;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "price")
    @Setter
    private BigDecimal price;
}

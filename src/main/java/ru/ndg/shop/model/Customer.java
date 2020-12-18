package ru.ndg.shop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customers", indexes = {
        @Index(name = "IX_customer_first_name", columnList = "first_name"),
        @Index(name = "IX_customer_last_name", columnList = "last_name"),
        @Index(name = "IX_customer_first_name_last_name", columnList = "first_name, last_name"),
        @Index(name = "IX_customer_email", columnList = "email")})
@NoArgsConstructor
@Getter
@ToString
public class Customer extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    @Setter
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Setter
    private String lastName;

    @Column(name = "email", unique = true)
    @Setter
    private String email;

    @Column(name = "address")
    @Setter
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;
}

package ru.ndg.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ndg.shop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

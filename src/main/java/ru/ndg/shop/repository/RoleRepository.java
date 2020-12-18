package ru.ndg.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ndg.shop.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

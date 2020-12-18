package ru.ndg.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ndg.shop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

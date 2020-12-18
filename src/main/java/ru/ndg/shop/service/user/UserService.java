package ru.ndg.shop.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ndg.shop.dto.UserDto;
import ru.ndg.shop.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto saveOrUpdate(UserDto userDto);
    void delete(Long id);
    User findByUsername(String username);
}

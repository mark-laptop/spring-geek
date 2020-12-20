package ru.ndg.shop.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ndg.shop.config.mapper.MapperFacade;
import ru.ndg.shop.dto.UserDto;
import ru.ndg.shop.exception.ProductNotFoundException;
import ru.ndg.shop.model.Role;
import ru.ndg.shop.model.User;
import ru.ndg.shop.repository.RoleRepository;
import ru.ndg.shop.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MapperFacade mapperFacade;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           MapperFacade mapperFacade,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapperFacade = mapperFacade;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return mapperFacade.mapAsList(userRepository.findAll(), UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Not found user by id: " + id));
        return mapperFacade.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto saveOrUpdate(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getId() == null) {
            Role role = roleRepository.findById(1L).orElseThrow(() -> new ProductNotFoundException("Role user not found"));
            User user = mapperFacade.map(userDto, User.class);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            userDto = mapperFacade.map(
                    userRepository.save(user), UserDto.class);
        } else {
            final Long id = userDto.getId();
            User user = userRepository.findById(userDto.getId()).orElseThrow(() ->
                    new ProductNotFoundException("Not found user by id: " + id));
            mapperFacade.mapObject(userDto, user);
        }
        return userDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Not found user by username: " + username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), mapRolesToGrantedAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }
}






















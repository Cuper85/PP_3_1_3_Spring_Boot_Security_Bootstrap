package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Role> roles = user.getRoles();
            {
                for (Role role : roles) {
                    Hibernate.initialize(role);
                }
            }
            ;
        }
        return users;
    }

    @Override
    @Transactional
    public void add(User user, List<Role> roles) {
        if (userRepository.findByUseremail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (roles == null || roles.isEmpty()) {
            Role defaultRole = roleRepository.findById(2L).orElse(null);
            if (defaultRole != null) {
                roles = List.of(defaultRole);
            }
        }
        List<Role> managedRoles = roles.stream()
                .map(role -> roleRepository.findById(role.getId()).orElseThrow(() -> new EntityNotFoundException("Role not found")))
                .collect(Collectors.toList());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(managedRoles);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(User updatedUser, List<Role> roles) {
        if (updatedUser == null || updatedUser.getId() == null) {
            throw new IllegalArgumentException("User ID не может быть null");
        }
        User existingUser = getUserById(updatedUser.getId());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRoles(roles);
        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        } else {
            existingUser.setPassword(existingUser.getPassword());
        }
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID: " + id + " not found"));
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUseremail(String email) {
        return userRepository.findByUseremail(email);
    }
}

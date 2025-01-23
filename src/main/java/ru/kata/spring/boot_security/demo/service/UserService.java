package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface UserService {

    List<User> listUsers();

    void add(User user, List<Role> roles);

    User getUserById(Long id);

    void update(User user, List<Role> roles);

    void delete(Long id);

    User findById(Long id);

    User findByUseremail(String email);
}

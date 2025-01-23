package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //  Optional<Role> findByName(String name);     в данном случае не используется, но как вариант можем искать по имени роли
}

package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void add(User user, List<Role> roles) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
        // Обновление пароля только в случае его изменения
        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        } else {
            existingUser.setPassword(existingUser.getPassword()); // Иначе сохраняем существующий пароль
        }
        userRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID: " + id + " not found"); // Проверка на существование
        }
        userRepository.deleteById(id);
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

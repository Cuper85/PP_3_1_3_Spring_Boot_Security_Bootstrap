package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Authentication authentication, Model model) {
        List<User> userList = userService.listUsers();
        User loggedUser = userService.findByUseremail(authentication.getName());
        model.addAttribute("user", loggedUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("users", userList);
        return "admin";
    }

    @GetMapping("/")
    public String showAddUserForm(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin";
    }

    @PostMapping("/users")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam("roleIds") List<Long> roleIds, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "admin";
        }
        List<Role> roles = new ArrayList<>(roleService.getRolesByIds(roleIds));
        userService.add(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam("roleIds") List<Long> roleIds, Model model) {
        if (user == null || id == null) {
            model.addAttribute("Error", "Пользователь не найден");
            return "admin";
        }
        List<Role> roles = roleService.getRolesByIds(roleIds);
        if (roles.isEmpty()) {
            model.addAttribute("error", "Роли не найдены");
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin";
        }
        User existingUser = userService.findByUseremail(user.getEmail());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists!");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin";
        }
        user.setId(id);
        userService.update(user, roles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}

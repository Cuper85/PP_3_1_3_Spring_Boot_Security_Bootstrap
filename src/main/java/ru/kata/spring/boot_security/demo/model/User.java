package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
@Table(name = "users_bootstrap")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "FirstName не должно быть пустым")
    @Size(min = 2, max = 15, message = "FirstName должно быть длиной от 2 до 15 символов")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "LastName не должно быть пустым")
    @Size(min = 2, max = 15, message = "LastName должно быть длиной от 2 до 15 символов")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be a positive number")
    @Column(name = "age")
    private Integer age;

    @NotBlank(message = "Password не должно быть пустым")
    @Size(min = 3, message = "Password должен быть не менее 3 символов")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Email не должно быть пустым")
    @Email(message = "Email должен быть действительным")
    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles_bootstrap",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public User() {}

    public User(String firstName, String lastName, int age, String password, String email, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public void setRoles(List<Role> roles) { this.roles = roles; }

    public List<Role> getRoles() { return roles; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}

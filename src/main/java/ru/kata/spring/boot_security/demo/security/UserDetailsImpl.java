package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {                 //  метод говорит от том, что этот аккаунт не просрочен, активен
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {                  //  метод говорит от том, что этот аккаунт не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {             //  метод говорит от том, что этот пароль не просрочен, активен
        return true;
    }

    @Override
    public boolean isEnabled() {                           //  метод говорит от том, что этот аккаунт включён и работает
        return true;
    }
}

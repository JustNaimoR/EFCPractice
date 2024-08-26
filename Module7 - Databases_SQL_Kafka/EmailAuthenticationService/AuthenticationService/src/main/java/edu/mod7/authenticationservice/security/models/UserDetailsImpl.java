package edu.mod7.authenticationservice.security.models;

import edu.mod7.authenticationservice.models.Client;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Client client;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "password";      // Без пароля
    }

    @Override
    public String getUsername() {
        return client.getUsername();
    }
}
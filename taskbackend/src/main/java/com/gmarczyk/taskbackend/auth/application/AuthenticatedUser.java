package com.gmarczyk.taskbackend.auth.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {

    private final String username;
    private final String password; // encrypted

    // Temporary, basic authenticated user details structure

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        });
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

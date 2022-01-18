package com.gmarczyk.taskbackend.configuration;

import com.gmarczyk.taskbackend.auth.application.AuthenticatedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfiguration {

    private static final String TMP_ADMIN_USERNAME = "admin";
    private static final String TMP_ADMIN_PASSWORD = "admin1";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // UserRepository.findBy(username) would be used here

                if (username.equals(TMP_ADMIN_USERNAME)) {
                    return new AuthenticatedUser(TMP_ADMIN_PASSWORD, passwordEncoder.encode("admin1"));
                } else {
                    throw new UsernameNotFoundException("Username " + username + " not found");
                }
            }
        };
    }

}

package bbojk.sideprojectplatformbackend.auth.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class TestConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        UserDetails userDetails = User.withUsername("test")
                .password(encodedPassword)
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}

package bbojk.sideprojectplatformbackend.auth.server;

import bbojk.sideprojectplatformbackend.auth.TestUserConstant;
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
        String encodedPassword = passwordEncoder.encode(TestUserConstant.TEST_PASSWORD);
        UserDetails userDetails = User.withUsername(TestUserConstant.TEST_USERNAME)
                .password(encodedPassword)
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}

package bbojk.sideprojectplatformbackend.auth.server;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TokenIssueSecurityConfig {
    private final AuthorizationServerConfigurer authorizationServerConfigurer;
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/token");
        httpSecurity.with(authorizationServerConfigurer, Customizer.withDefaults());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}

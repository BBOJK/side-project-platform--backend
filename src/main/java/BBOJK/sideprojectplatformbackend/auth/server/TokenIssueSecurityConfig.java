package bbojk.sideprojectplatformbackend.auth.server;

import bbojk.sideprojectplatformbackend.auth.SecurityOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TokenIssueSecurityConfig {
    public static final RequestMatcher TOKEN_REQUEST_MATCHER = new AntPathRequestMatcher("/token");
    private final AuthorizationServerConfigurer authorizationServerConfigurer;

    @Bean
    @Order(SecurityOrder.TOKEN_ISSUE_ORDER)
    public SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(TOKEN_REQUEST_MATCHER)
                .with(authorizationServerConfigurer, Customizer.withDefaults());
        return httpSecurity.build();
    }
}

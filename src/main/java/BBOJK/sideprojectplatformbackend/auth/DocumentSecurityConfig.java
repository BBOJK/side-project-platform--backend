package bbojk.sideprojectplatformbackend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class DocumentSecurityConfig {

    public static final RequestMatcher DOCUMENT_REQUEST_MATCHER = new OrRequestMatcher(
            new AntPathRequestMatcher("/", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/index.html", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/swagger-ui.html", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/css/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/js/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/favicon/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/openapi3.yaml", HttpMethod.GET.name())
    );

    @Bean
    @Order(SecurityOrder.DOCUMENT_ORDER)
    public SecurityFilterChain documentSecurity(HttpSecurity http) throws Exception {
        return http.securityMatcher(DOCUMENT_REQUEST_MATCHER)
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .build();
    }
}

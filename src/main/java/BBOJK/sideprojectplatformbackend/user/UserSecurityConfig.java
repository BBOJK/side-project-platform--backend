package bbojk.sideprojectplatformbackend.user;

import bbojk.sideprojectplatformbackend.auth.SecurityOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@Slf4j
public class UserSecurityConfig {

  public static final RequestMatcher USER_REGISTER_REQUEST_MATCHER = new OrRequestMatcher(
          new AntPathRequestMatcher("/user/", HttpMethod.POST.name())
  );

    @Bean
    @Order(SecurityOrder.USER_REGISTER_ORDER)
    public SecurityFilterChain userRegisterSecurity(HttpSecurity http) throws Exception {
        return http.securityMatcher(USER_REGISTER_REQUEST_MATCHER)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .build();
    }

}

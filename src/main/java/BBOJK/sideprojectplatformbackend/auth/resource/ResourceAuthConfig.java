package bbojk.sideprojectplatformbackend.auth.resource;

import bbojk.sideprojectplatformbackend.auth.SecurityOrder;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceAuthConfig {
    JWTProcessor<SecurityContext> jwtProcessor;


    @Autowired
    public ResourceAuthConfig(JWSKeySelector<SecurityContext> jwsKeySelector) {
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        this.jwtProcessor = jwtProcessor;
    }

    @Bean
    @Order(SecurityOrder.RESOURCE_ACCESS_ORDER)
    public SecurityFilterChain resourceAuth(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        request -> request
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        resource -> resource.jwt(
                                jwt -> jwt.decoder(jwtDecoder())
                        )
                ).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(jwtProcessor);
    }
}

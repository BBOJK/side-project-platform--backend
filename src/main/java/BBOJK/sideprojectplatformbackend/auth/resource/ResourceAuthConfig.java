package bbojk.sideprojectplatformbackend.auth.resource;

import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

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
    public SecurityFilterChain resourceAuth(HttpSecurity http) throws Exception {
        http.securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/token")));
        http
                .authorizeHttpRequests(
                        request -> request.anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        resource -> resource.jwt(
                                jwt -> jwt.decoder(jwtDecoder())
                        )
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(jwtProcessor);
    }
}

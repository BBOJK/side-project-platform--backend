package bbojk.sideprojectplatformbackend.auth;

import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.JwtAuthSetting;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.SingleKeyJWSKeySelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryJWSConfig {
    private final RSAKey rsaKey;

    public InMemoryJWSConfig() {
        try {
            rsaKey = new RSAKeyGenerator(2048).generate();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JWSSigner rsaSigner() throws JOSEException {
        return new RSASSASigner(rsaKey);
    }

    @Bean
    public JWSKeySelector<SecurityContext> jwsKeySelector() throws JOSEException {
        return new SingleKeyJWSKeySelector<>(JwtAuthSetting.SIGNING_ALGORITHM, rsaKey.toPublicKey());
    }
}

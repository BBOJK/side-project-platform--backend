package bbojk.sideprojectplatformbackend.auth.server.test;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jca.JCAContext;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.util.Base64URL;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestJWSProvider implements JWSSigner, JWSVerifier {
    private final RSASSASigner signer;
    private final RSASSAVerifier verifier;

    public TestJWSProvider() {
        try {
            RSAKey rsaKey = new RSAKeyGenerator(2048).generate();
            signer = new RSASSASigner(rsaKey);
            verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Base64URL sign(JWSHeader header, byte[] signingInput) throws JOSEException {
        return signer.sign(header, signingInput);
    }

    @Override
    public boolean verify(JWSHeader header, byte[] signingInput,
                          Base64URL signature) throws JOSEException {
        return verifier.verify(header, signingInput, signature);
    }

    @Override
    public Set<JWSAlgorithm> supportedJWSAlgorithms() {
        return Set.of(JWSAlgorithm.RS256);
    }

    @Override
    public JCAContext getJCAContext() {
        return new JCAContext();
    }
}

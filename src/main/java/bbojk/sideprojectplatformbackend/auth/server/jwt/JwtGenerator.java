package bbojk.sideprojectplatformbackend.auth.server.jwt;

import bbojk.sideprojectplatformbackend.auth.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.TokenExpirationTimer;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtGenerator {
    private static final String ISSUER = "bbojk";
    private static final String AUDIENCE = ISSUER;
    private static final JWSAlgorithm SIGNING_ALGORITHM = JWSAlgorithm.RS256;
    @Qualifier("accessTokenTimer")
    private final TokenExpirationTimer timer;
    private final JWSSigner jwsSigner;

    public Jwt generateTo(String username, Map<String, String> claimsMap) {
        JWSHeader header = new JWSHeader(SIGNING_ALGORITHM);
        JWTClaimsSet claimsSet = convert(username, claimsMap);

        return new Jwt(serialize(header, claimsSet), timer.now(), timer.getExpiresAt());
    }

    private JWTClaimsSet convert(String subject, Map<String, String> claims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.issuer(ISSUER)
                .audience(AUDIENCE)
                .audience(subject)
                .issueTime(Date.from(timer.now()))
                .expirationTime(Date.from(timer.getExpiresAt()));
        claims.forEach(builder::claim);
        return builder.build();
    }

    private String serialize(JWSHeader header, JWTClaimsSet claimsSet) {
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);


        try {
            signedJWT.sign(jwsSigner);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }
}

package bbojk.sideprojectplatformbackend.auth.server.authentication.jwt;

import bbojk.sideprojectplatformbackend.auth.server.TokenExpirationTimer;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
    @Qualifier("accessTokenTimer")
    private final TokenExpirationTimer timer;
    private final JWSSigner jwsSigner;

    public Jwt generateTo(String username, Map<String, String> claimsMap) {
        JWSHeader header = new JWSHeader(JwtAuthSetting.SIGNING_ALGORITHM);
        JWTClaimsSet claimsSet = convert(username, claimsMap);

        return new Jwt(serialize(header, claimsSet), timer.now(), timer.getExpiresAt());
    }

    private JWTClaimsSet convert(String subject, Map<String, String> claims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.issuer(JwtAuthSetting.ISSUER)
                .audience(JwtAuthSetting.AUDIENCE)
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

package bbojk.sideprojectplatformbackend.auth.server.jwt;

import com.nimbusds.jose.JWSAlgorithm;

public class JwtAuthSetting {
    public static final String ISSUER = "bbojk";
    public static final String AUDIENCE = ISSUER;
    public static final JWSAlgorithm SIGNING_ALGORITHM = JWSAlgorithm.RS256;
}
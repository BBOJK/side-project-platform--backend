package bbojk.sideprojectplatformbackend.auth;

import java.time.Instant;

public class Jwt extends AbstractToken {
    public Jwt(String value, Instant issuedAt, Instant expiresAt) {
        super(value, issuedAt, expiresAt);
    }
}

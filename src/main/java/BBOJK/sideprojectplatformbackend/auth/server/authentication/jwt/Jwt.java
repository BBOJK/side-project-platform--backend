package bbojk.sideprojectplatformbackend.auth.server.authentication.jwt;

import bbojk.sideprojectplatformbackend.auth.server.AbstractToken;

import java.time.Instant;

public class Jwt extends AbstractToken {
    public Jwt(String value, Instant issuedAt, Instant expiresAt) {
        super(value, issuedAt, expiresAt);
    }
}

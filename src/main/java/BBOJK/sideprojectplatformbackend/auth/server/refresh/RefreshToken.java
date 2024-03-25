package bbojk.sideprojectplatformbackend.auth.server.refresh;

import bbojk.sideprojectplatformbackend.auth.server.AbstractToken;

import java.time.Instant;

public class RefreshToken extends AbstractToken {
    public RefreshToken(String value, Instant issuedAt, Instant expiresAt) {
        super(value, issuedAt, expiresAt);
    }
}

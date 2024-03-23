package bbojk.sideprojectplatformbackend.auth;

import java.time.Instant;

public interface Token {
    String getValue();
    Instant getIssuedAt();
    Instant getExpiresAt();
}

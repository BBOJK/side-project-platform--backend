package bbojk.sideprojectplatformbackend.auth.server;

import java.time.Instant;

public interface Token {
    String getValue();
    Instant getIssuedAt();
    Instant getExpiresAt();
}

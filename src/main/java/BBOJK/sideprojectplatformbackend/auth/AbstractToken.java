package bbojk.sideprojectplatformbackend.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public abstract class AbstractToken implements Token {
    private final String value;
    private final Instant issuedAt;
    private final Instant expiresAt;
}

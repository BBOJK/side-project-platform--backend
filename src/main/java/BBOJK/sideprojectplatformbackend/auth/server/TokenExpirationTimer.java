package bbojk.sideprojectplatformbackend.auth.server;

import java.time.Instant;

public interface TokenExpirationTimer {

    Instant now();

    Instant getExpiresAt();

}

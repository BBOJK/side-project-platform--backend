package bbojk.sideprojectplatformbackend.auth.server;

import java.time.Duration;
import java.time.Instant;

/**
 * Abstract token timer for general implementations to extend
 */
public abstract class AbstractTokenExpirationTimer implements TokenExpirationTimer {
    private final Duration duration;

    protected AbstractTokenExpirationTimer(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Instant now() {
        return Instant.now();
    }

    @Override
    public Instant getExpiresAt() {
        return Instant.now().plus(duration);
    }

}

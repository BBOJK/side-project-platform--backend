package bbojk.sideprojectplatformbackend.auth.server.authentication.refresh;

import bbojk.sideprojectplatformbackend.auth.server.AbstractTokenExpirationTimer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenTimer extends AbstractTokenExpirationTimer {
    protected RefreshTokenTimer() {
        super(Duration.ofDays(30));
    }
}

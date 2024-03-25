package bbojk.sideprojectplatformbackend.auth.server.authentication.jwt;

import bbojk.sideprojectplatformbackend.auth.server.AbstractTokenExpirationTimer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AccessTokenTimer extends AbstractTokenExpirationTimer {
    protected AccessTokenTimer() {
        super(Duration.ofMinutes(30));
    }
}

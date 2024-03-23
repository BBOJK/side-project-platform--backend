package bbojk.sideprojectplatformbackend.auth.server.refresh;

import bbojk.sideprojectplatformbackend.auth.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.TokenExpirationTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RefreshTokenGenerator {
    @Qualifier("refreshTokenTimer")
    private final TokenExpirationTimer timer;

    private final StringKeyGenerator refreshTokenGenerator =
            new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    public RefreshToken generate() {
        return new RefreshToken(
            refreshTokenGenerator.generateKey(),
            timer.now(),
            timer.getExpiresAt()
        );
    }
}

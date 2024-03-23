package bbojk.sideprojectplatformbackend.auth.server.http;

import bbojk.sideprojectplatformbackend.auth.Jwt;
import bbojk.sideprojectplatformbackend.auth.RefreshToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AccessTokenResponse {
    private final Jwt jwt;
    private final RefreshToken refreshToken;
}

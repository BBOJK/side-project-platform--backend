package bbojk.sideprojectplatformbackend.auth.server.authentication;

import bbojk.sideprojectplatformbackend.auth.server.AccessTokenParameterNames;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

public class RefreshTokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String refreshToken = request.getParameter(AccessTokenParameterNames.REFRESH_TOKEN);
        if (!StringUtils.hasText(refreshToken)) {
            return null;
        }

        return new RefreshTokenAuthenticationToken(refreshToken);
    }
}

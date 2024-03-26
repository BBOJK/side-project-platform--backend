package bbojk.sideprojectplatformbackend.auth.server.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

/**
 * Authentication filter to process various authentication at once.
 */
@Slf4j
public class ConvertedAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationConverter authenticationConverter;

    public ConvertedAuthenticationProcessingFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationConverter authenticationConverter,
            AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = authenticationConverter.convert(request);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Given authentication request is not supported.");
            return null;
        }

        return getAuthenticationManager().authenticate(authentication);
    }
}

package bbojk.sideprojectplatformbackend.auth.server.authentication.formlogin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

public class UsernamePasswordAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!isPostFormLogin(request)) {
            return null;
        }

        String username = request.getParameter("username");
        if (!StringUtils.hasText(username)) {
            return null;
        }

        String password = request.getParameter("password");
        if (!StringUtils.hasText(password)) {
            return null;
        }

        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }

    private boolean isPostFormLogin(HttpServletRequest request) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        String method = request.getMethod();
        return contentType != null
                && MediaType.APPLICATION_FORM_URLENCODED.includes(MediaType.valueOf(contentType))
                && method.equals(HttpMethod.POST.name());
    }
}

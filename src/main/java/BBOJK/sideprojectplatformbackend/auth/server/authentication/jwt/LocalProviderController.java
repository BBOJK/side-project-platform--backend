package bbojk.sideprojectplatformbackend.auth.server.authentication.jwt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Controller for <a href="https://developers.google.com/identity/openid-connect/openid-connect#discovery">
 *     /.well-known/openid-configuration endpoint</a> and jwks_uri
 */
@Controller
public class LocalProviderController {


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    static class WellKnownDto {
        private static final String WELL_KNOWN_URI = ".well-known/openid-configuration";

        @Value("${local.jwt.issuer}")
        private String issuer;
        @Value("${local.jwt.issuer}/token")
        private String tokenEndpoint;

        private String jwksUri;
        private List<String> idTokenSigningAlgValuesSupported;


    }
}

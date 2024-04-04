package bbojk.sideprojectplatformbackend.auth.server.http.converter;

import bbojk.sideprojectplatformbackend.auth.server.authentication.jwt.Jwt;
import bbojk.sideprojectplatformbackend.auth.server.authentication.refresh.RefreshToken;
import bbojk.sideprojectplatformbackend.auth.server.AccessTokenParameterNames;
import bbojk.sideprojectplatformbackend.auth.server.http.AccessTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter}
 * of {@link AccessTokenResponse} only for writing
 */
@Component
public class AccessTokenResponseHttpMessageConverter
        extends AbstractHttpMessageConverter<AccessTokenResponse> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final ObjectMapper objectMapper;

    public AccessTokenResponseHttpMessageConverter(ObjectMapper objectMapper) {
        super(DEFAULT_CHARSET, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return AccessTokenResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected AccessTokenResponse readInternal(Class<? extends AccessTokenResponse> clazz,
                                               HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new UnsupportedOperationException("reading is not supported in the AccessTokenResponseHttpMessageConverter");
    }

    @Override
    protected void writeInternal(AccessTokenResponse accessTokenResponse,
                                 HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Jwt jwt = accessTokenResponse.getJwt();
        long expiresIn = ChronoUnit.SECONDS.between(jwt.getIssuedAt(), jwt.getExpiresAt());
        RefreshToken refreshToken = accessTokenResponse.getRefreshToken();
        Map<String, Object> outputMap = Map.of(
                AccessTokenParameterNames.ACCESS_TOKEN, jwt.getValue(),
                AccessTokenParameterNames.EXPIRES_IN, expiresIn,
                AccessTokenParameterNames.REFRESH_TOKEN, refreshToken.getValue()
        );

        outputMessage.getBody().write(objectMapper.writeValueAsBytes(outputMap));
    }
}

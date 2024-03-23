package bbojk.sideprojectplatformbackend.auth.server.authorization;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenAuthorizationService implements TokenAuthorizationService {
    final Map<String, TokenAuthorization> authorizations = new ConcurrentHashMap<>();

    @Override
    public void save(TokenAuthorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        authorizations.put(authorization.getId(), authorization);
    }

    @Override
    public void remove(TokenAuthorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        authorizations.remove(authorization.getId());
    }

    @Override
    public Optional<TokenAuthorization> findByRefreshToken(String refreshToken) {
        for (TokenAuthorization authorization : authorizations.values()) {
            if (hasRefreshToken(authorization, refreshToken)) {
                return Optional.of(authorization);
            }
        }
        return Optional.empty();
    }

    private boolean hasRefreshToken(TokenAuthorization authorization, String refreshToken) {
        return authorization.getRefreshToken().getValue().equals(refreshToken);
    }
}

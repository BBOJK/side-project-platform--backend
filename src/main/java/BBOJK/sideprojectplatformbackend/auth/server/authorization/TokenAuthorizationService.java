package bbojk.sideprojectplatformbackend.auth.server.authorization;


import java.util.Optional;

/**
 * @see TokenAuthorization
 */
public interface TokenAuthorizationService {

    void save(TokenAuthorization authorization);

    void remove(TokenAuthorization authorization);

    Optional<TokenAuthorization> findByRefreshToken(String refreshToken);
}

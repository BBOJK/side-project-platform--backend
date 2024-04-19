package bbojk.sideprojectplatformbackend.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Role implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public static Role createDefaultRole(){
        return new Role("USER");
    }
}

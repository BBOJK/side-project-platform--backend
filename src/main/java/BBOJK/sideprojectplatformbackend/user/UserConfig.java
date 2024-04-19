package bbojk.sideprojectplatformbackend.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@Profile(value = "default")
public class UserConfig {

    @Bean
    public UserDetailsManager userDetailsManager(){
        return new InMemoryUserDetailsManager();
    }

}

package bbojk.sideprojectplatformbackend.user.service;

import bbojk.sideprojectplatformbackend.user.User;
import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationRequestDto;
import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDetailsManager userDetailsManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public RegistrationResponseDto createUser(RegistrationRequestDto requestDto) {

    User user = User.createUser(
            requestDto.getUsername(),
            passwordEncoder.encode(requestDto.getPassword())
    );
    userDetailsManager.createUser(user);

    return RegistrationResponseDto.builder()
            .username(user.getUsername())
            .build();

  }

  @Override
  public boolean isExists(String username) {
    return userDetailsManager.userExists(username);
  }

}

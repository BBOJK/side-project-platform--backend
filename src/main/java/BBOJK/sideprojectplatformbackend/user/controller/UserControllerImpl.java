package bbojk.sideprojectplatformbackend.user.controller;

import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationRequestDto;
import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationResponseDto;
import bbojk.sideprojectplatformbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

  private final UserService userService;

  @Override
  public ResponseEntity<RegistrationResponseDto> register(RegistrationRequestDto requestDto) {
    return ResponseEntity.ok(
            userService.createUser(requestDto)
    );
  }
}

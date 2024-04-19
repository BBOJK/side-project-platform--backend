package bbojk.sideprojectplatformbackend.user.service;

import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationRequestDto;
import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationResponseDto;

public interface UserService {
    RegistrationResponseDto createUser(RegistrationRequestDto requestDto);
    boolean isExists(String username);
}
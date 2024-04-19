package bbojk.sideprojectplatformbackend.user.controller;

import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserController {

    @PostMapping("/")
    ResponseEntity register(
            @Valid @RequestBody
            RegistrationRequestDto requestDto
    );

}
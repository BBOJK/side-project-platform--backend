package bbojk.sideprojectplatformbackend.user.service;

import bbojk.sideprojectplatformbackend.user.controller.dto.RegistrationRequestDto;
import bbojk.sideprojectplatformbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void createUserTest(){
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setUsername("test_username");
        requestDto.setPassword("test_password");
        userService.createUser(requestDto);
        assertEquals(userService.isExists("test_username"), true);
    }

}
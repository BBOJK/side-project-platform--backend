package bbojk.sideprojectplatformbackend.user.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponseDto {
    private String username;
}

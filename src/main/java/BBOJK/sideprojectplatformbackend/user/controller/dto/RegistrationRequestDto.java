package bbojk.sideprojectplatformbackend.user.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
public class RegistrationRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
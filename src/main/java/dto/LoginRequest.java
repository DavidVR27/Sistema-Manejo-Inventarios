package dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {

    @NotBlank(message = "Correo es requerido")
    private String email;

    @NotBlank(message = "Contraseña es requerida")
    private String password;
}

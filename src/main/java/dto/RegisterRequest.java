package dto;

import enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "Correo es requerido")
    private String email;

    @NotBlank(message = "Contraseña es requerida")
    private String password;

    @NotBlank(message = "Número de Teléfono es requerido")
    private String phoneNumber;

    private UserRole role;
}

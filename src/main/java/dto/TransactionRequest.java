package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @Positive(message = "El ID del Producto debe ser positivo")
    private Long productId;

    @Positive(message = "La cantidad del Producto debe ser positiva")
    private Integer quantity;

    private Long supplierId;

    private String description;
}

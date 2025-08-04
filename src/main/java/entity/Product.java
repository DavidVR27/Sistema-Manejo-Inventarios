package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "SKU es requerido")
    @Column(unique = true)
    private String sku;

    @Positive(message = "El precio del producto debe ser un valor positivo")
    private BigDecimal price;

    @Min(value = 0, message = "Stock no puede ser menor a cero")
    private Integer stockQuantity;

    private String description;

    private String imageURL;
    private LocalDateTime expiryDate;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", expiryDate=" + expiryDate +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}

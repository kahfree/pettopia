package a2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_items")
public class OrderItem  implements Serializable {

    @Id
    @NotNull
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "quantity")
    private Integer quantity;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne
    @JsonBackReference(value="order-item-collection")
    @ToString.Exclude
    private Orders orderId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @JsonManagedReference(value="orderitem-product")
    @ToString.Exclude
    private Product productId;
}

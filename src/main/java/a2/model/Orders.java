package a2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Orders implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;

    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    @JsonBackReference(value="order-collection")
    private Customer customerId;

    @JoinColumn(name = "order_status_id", referencedColumnName = "order_status_id")
    @ManyToOne(optional = false)
    @JsonBackReference("orderstatus-order")
    @ToString.Exclude
    private OrderStatus orderStatusId;

    @OneToMany(mappedBy = "orderId")
    @JsonManagedReference(value="order-item-collection")
    @ToString.Exclude
    private List<OrderItem> orderItemCollection;
}

package a2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pets")
public class Pet implements Serializable {


    @Id
    @NotNull
    @Column(name = "pet_id")
    private Integer petId;

    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @JoinColumn(name = "pet_health_id", referencedColumnName = "pet_health_id")
    @OneToOne(optional = false)
    @JsonBackReference(value="pet-pethealth")
    @ToString.Exclude
    private PetHealth petHealth;

    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne(optional = false)
    @JsonBackReference(value="customer-pet")
    @ToString.Exclude
    private Customer customerId;
}

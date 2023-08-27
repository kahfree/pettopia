package a2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "customers")
public class Customer extends RepresentationModel<Customer> implements Serializable {

    @Id
    @NotNull
    @Column(name = "customer_id")
    private Integer customerId;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 50)
    @NotNull
    @Column(name = "last_name")
    private String lastName;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//consider using this annotation to enforce field validation
    @Size(max = 255)
    @NotNull
    @Column(name = "email")

    private String email;

    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "phone")
    private String phone;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Size(max = 50)
    @Column(name = "city")
    private String city;

    @Size(max = 50)
    @Column(name = "country")
    private String country;

    @Size(max = 10)
    @Column(name = "postcode")
    private String postcode;

    @OneToMany(mappedBy = "customerId")
    @JsonManagedReference(value="review-collection")
    @JsonProperty(access =JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<Review> reviewCollection;

    @OneToMany(mappedBy = "customerId")
    @JsonManagedReference(value="order-collection")
    @JsonProperty(access =JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<Orders> ordersCollection;

    @OneToMany(mappedBy = "customerId")
    @JsonManagedReference(value="customer-pet")
    @JsonProperty(access =JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<Pet> petCollection;

    @OneToMany(mappedBy = "customerId")
    @JsonManagedReference(value="customer-event")
    @JsonProperty(access =JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<Event> eventCollection;
}

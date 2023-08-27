package a2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "events")
public class Event implements Serializable {


    @Id
    @NotNull
    @Column(name = "event_id")
    private Integer eventId;

    @Size(max = 50)
    @Column(name = "title")
    private String title;

    @Column(name = "date_of_event")
    private Date dateOfEvent;

    @Size(max=100)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne(optional = false)
    @JsonBackReference(value="customer-event")
    @JsonProperty(access =JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private Customer customerId;
}

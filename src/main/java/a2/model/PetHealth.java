package a2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pet_health")
public class PetHealth implements Serializable {


    @Id
    @NotNull
    @Column(name = "pet_health_id")
    private Integer petHealthId;

    @NotNull
    @Column(name = "vaccinated")
    private Boolean vaccinated;

    @NotNull
    @Column(name = "last_check_up")
    @Temporal(TemporalType.DATE)
    private Date lastCheckUp;

    @NotNull
    @Column(name = "next_check_up")
    @Temporal(TemporalType.DATE)
    private Date nextCheckUp;

    @Size(max=100)
    @Column(name = "notes")
    private String notes;

}

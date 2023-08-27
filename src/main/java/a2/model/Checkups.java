package a2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "checkups")
public class Checkups implements Serializable {


    @Id
    @NotNull
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @NotNull
    @Column(name = "pet_id")
    private Integer pet;
}

package guru.springframework.sfgpetclinic.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="visits")
public class Visit extends BaseEntity {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String description;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Builder
    public Visit(Long id, LocalDate date, String description, Pet pet) {
        super(id);
        this.date = date;
        this.description = description;
        this.pet = pet;
    }
}

package guru.springframework.sfgpetclinic.model;

import javax.persistence.Entity;

@Entity
public class Speciality extends BaseEntity{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

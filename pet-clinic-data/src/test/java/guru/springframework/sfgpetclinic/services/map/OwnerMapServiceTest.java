package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OwnerMapServiceTest {
    OwnerMapService ownerMapService;
    private Owner defaultOwner;

    @BeforeEach
    void setUp() {
        defaultOwner = Owner.builder().id(1L).lastName("George").build();
        ownerMapService = new OwnerMapService(new PetMapService(new PetTypeMapService()));
        ownerMapService.save(defaultOwner);
    }

    @Test
    void findAll() {
        Set<Owner> owners = ownerMapService.findAll();
        Assertions.assertEquals(1, owners.size());
    }

    @Test
    void deleteById() {
        ownerMapService.deleteById(defaultOwner.getId());
        Assertions.assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void delete() {
        ownerMapService.delete(defaultOwner);
        Assertions.assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void save() {
        Long id = 2L;
        Owner owner = Owner.builder().id(id).build();
        Owner savedOwner = ownerMapService.save(owner);
        Assertions.assertEquals(id, savedOwner.getId());
    }

    @Test
    void findById() {
        Owner owner = ownerMapService.findById(defaultOwner.getId());
        Assertions.assertEquals(defaultOwner.getId(), owner.getId());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerMapService.findByLastName(defaultOwner.getLastName());
        Assertions.assertEquals(defaultOwner.getLastName(), owner.getLastName());
    }
}
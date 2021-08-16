package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile({"default", "map"})
public class PetMapService extends AbstractMapService<Pet, Long> implements PetService {
    private final PetTypeService petTypeService;

    public PetMapService(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public Set<Pet> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Pet object) {
        super.delete(object);
    }

    @Override
    public Pet save(Pet object) {
        if(object != null){
            if(object.getPetType()!= null){
                if(object.getPetType().getId() == null){
                    object.setPetType(petTypeService.save(object.getPetType()));
                }
            }else{
                throw new RuntimeException("PetType is required");
            }
            return super.save(object);
        }else {
            return null;
        }
    }

    @Override
    public Pet findById(Long id) {
        return super.findById(id);
    }
}

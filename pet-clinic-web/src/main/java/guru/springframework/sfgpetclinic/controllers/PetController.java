package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping("/owners/{ownerId}/pets")
@Controller
public class PetController {
    public static final String VIEW_PETS_FORM = "pets/form";
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;
    private final PetService petService;

    public PetController(PetTypeService petTypeService, OwnerService ownerService, PetService petService) {
        this.petTypeService = petTypeService;
        this.ownerService = ownerService;
        this.petService = petService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("types")
    public Set<PetType> populatePetTypes(){
        return petTypeService.findAll();
    }

    @ModelAttribute("owners")
    public Set<Owner> populateOwnerList(){
        return ownerService.findAll();
    }

    @ModelAttribute("owner")
    public Owner populateOwner(@PathVariable("ownerId") Long ownerId){
        return ownerService.findById(ownerId);
    }

    @RequestMapping("/new")
    public String initNewForm(Owner owner, Model model){
        model.addAttribute("pet", Pet.builder().owner(owner).build());
        return VIEW_PETS_FORM;
    }

    private String createOrUpdateAction(Pet pet, BindingResult result){
        if(result.hasErrors()){
            return VIEW_PETS_FORM;
        }
        Pet savedPet = petService.save(pet);
        return "redirect:/owners/"+savedPet.getOwner().getId();
    }

    @PostMapping("/new")
    public String performNew(Owner owner, @Valid Pet pet, BindingResult result){
        pet.setOwner(owner);
        return createOrUpdateAction(pet, result);
    }

    @RequestMapping("/{petId}/edit")
    public String initUpdateForm(@PathVariable("petId") Long petId, Model model){
        Pet pet = petService.findById(petId);
        model.addAttribute("pet", pet);
        return VIEW_PETS_FORM;
    }

    @PostMapping("/{petId}/edit")
    public String performEdit(Owner owner, @Valid Pet pet,@PathVariable("petId") Long petId, BindingResult result){
        pet.setId(petId);
        pet.setOwner(owner);
        return createOrUpdateAction(pet, result);
    }

}

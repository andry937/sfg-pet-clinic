package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
@Controller
public class VisitController {
    private final VisitService visitService;
    private final PetService petService;
    public static final String VIEW_VISIT_FORM = "visits/form";

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setDisallowedFields("id");
    }

    @ModelAttribute("pet")
    public Pet populatePet(@PathVariable("petId") Long petId){
        return petService.findById(petId);
    }

    @RequestMapping("/new")
    public String initCreateVisitForm(Pet pet, Model model){
        model.addAttribute("visit", Visit.builder().pet(pet).build());
        return VIEW_VISIT_FORM;
    }

    @RequestMapping("/{visitId}/edit")
    public String initUpdateVisitForm(@PathVariable("visitId")Long visitId, Model model){
        Visit visit = visitService.findById(visitId);
        model.addAttribute("visit", visit);
        return VIEW_VISIT_FORM;
    }

    private String createOrEditAction(Visit visit, BindingResult result){
        if(result.hasErrors()){
            return VIEW_VISIT_FORM;
        }
        Visit savedVisit = visitService.save(visit);
        Long ownerId = savedVisit.getPet().getOwner().getId();
        return String.format("redirect:/owners/%d", ownerId);
    }

    @PostMapping("/new")
    public String performCreateVisit(Pet pet, @Valid Visit visit, BindingResult result){
        visit.setPet(pet);
        return createOrEditAction(visit, result);
    }

    @PostMapping("/{visitId}/edit")
    public String performUpdateVisit(Pet pet,
                                     @PathVariable("visitId")Long visitId,
                                     @Valid Visit visit,
                                     BindingResult result){
        visit.setPet(pet);
        visit.setId(visitId);
        return createOrEditAction(visit, result);
    }
}

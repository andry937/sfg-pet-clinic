package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping("/owners")
@Controller
public class OwnerController {
    public static final String VIEW_OWNERS_FORM = "owners/form";
    public static final String VIEW_OWNERS_INDEX = "owners/index";
    public static final String VIEW_OWNERS_FIND = "owners/find";
    public static final String VIEW_OWNERS_DETAILS = "owners/details";
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void bindData(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping({"", "/","/index","/index.html"})
    public String listOwners(Model model){
        model.addAttribute("owners", ownerService.findAll());
        return VIEW_OWNERS_INDEX;
    }

    @RequestMapping("/{id}")
    public String showOwner(@PathVariable String id, Model model){
        model.addAttribute("owner", ownerService.findById(Long.valueOf(id)));
        return VIEW_OWNERS_DETAILS;
    }

    @RequestMapping("/find")
    public String showFindOwner(Model model){
        model.addAttribute("owner", new Owner());
        return VIEW_OWNERS_FIND;
    }

    @PostMapping("/find")
    public String findOwner(@ModelAttribute Owner owner, Model model, BindingResult bindingResult){
        Set<Owner> result  = ownerService.findAllByLastNameLike(owner.getLastName());
        if(result.size() == 1) {
            Owner ownerFound = result.stream().findFirst().orElse(new Owner());
            return "redirect:/owners/"+ownerFound.getId();
        }else if(result.isEmpty()) {
            bindingResult.rejectValue("lastName","error.notFound","No owners found");
            model.addAttribute("owner",owner);
            return VIEW_OWNERS_FIND;
        }else {
            model.addAttribute("owners", result);
            return VIEW_OWNERS_INDEX;
        }
    }

    @RequestMapping("/new")
    public String initCreateForm(Model model){
        model.addAttribute("owner", new Owner());
        return VIEW_OWNERS_FORM;
    }

    private String createOrUpdateAction(Owner owner, BindingResult result) {
        return createOrUpdateAction(owner, result, null);
    }

    private String createOrUpdateAction(Owner owner, BindingResult result, Long id){
        if(result.hasErrors()){
            return VIEW_OWNERS_FORM;
        }
        if(id !=null){
            owner.setId(id);
        }
        Owner savedOwner = ownerService.save(owner);
        return "redirect:/owners/"+savedOwner.getId();
    }

    @PostMapping("/new")
    public String performCreate(@Valid Owner owner, BindingResult result){
        return createOrUpdateAction(owner,result);
    }

    @PostMapping("/{id}/edit")
    public String performEdit(@Valid Owner owner, BindingResult result, @PathVariable Long id){
        return createOrUpdateAction(owner, result, id);
    }

    @RequestMapping("/{id}/edit")
    public String initUpdateForm(@PathVariable String id, Model model){
        Owner owner = ownerService.findById(Long.valueOf(id));
        model.addAttribute("owner", owner);
        return VIEW_OWNERS_FORM;
    }
}

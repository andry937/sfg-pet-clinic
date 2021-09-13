package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/owners")
@Controller
public class OwnerController {
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
        return "owners/index";
    }

    @RequestMapping("/{id}")
    public String showOwner(@PathVariable String id, Model model){
        model.addAttribute("owner", ownerService.findById(Long.valueOf(id)));
        return "owners/details";
    }

    @RequestMapping("/find")
    public String showFindOwner(Model model){
        model.addAttribute("owner", new Owner());
        return "owners/find";
    }

    @PostMapping("/find")
    public String findOwner(@ModelAttribute Owner owner, Model model){
        Set<Owner> result  = ownerService.findAllByLastNameLike(owner.getLastName());
        if(result.size() == 1) {
            Owner ownerFound = result.stream().findFirst().orElse(new Owner());
            return "redirect:/owners/"+ownerFound.getId();
        }else if(result.isEmpty()) {
            return "";
        }else {
            model.addAttribute("owners", result);
            return "owners/index";
        }
    }
}

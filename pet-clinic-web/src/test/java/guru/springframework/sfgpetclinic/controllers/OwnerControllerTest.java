package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OwnerControllerTest {
    private final String ownerLastName = "test 1";
    MockMvc mockMvc;
    OwnerController ownerController;
    @Mock
    OwnerService ownerService;

    Set<Owner> owners = new HashSet<>();

    Long id = 1L;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        ownerController = new OwnerController(ownerService);
        owners.add(Owner.builder().id(id).build());
        owners.add(Owner.builder().id(2L).build());

        mockMvc = MockMvcBuilders.standaloneSetup(ownerController)
                .build();

    }
    @Test
    void testListOwners() throws Exception {
        Mockito.when(ownerService.findAll()).thenReturn(owners);
        mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("owners", hasSize(owners.size())))
                .andExpect(view().name(OwnerController.VIEW_OWNERS_INDEX));
    }

    @Test
    void testShowOwners() throws Exception {
        Mockito.when(ownerService.findById(Mockito.anyLong())).thenReturn(owners.stream().findFirst().orElse(null));
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/"+id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(OwnerController.VIEW_OWNERS_DETAILS));
    }

    @Test
    void testShowFindOwners() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(OwnerController.VIEW_OWNERS_FIND));
        Mockito.verifyZeroInteractions(ownerService);
    }

    @Test
    void testFindOwnerOne() throws Exception {
        Set<Owner> results = owners.stream().limit(1).collect(Collectors.toSet());
        Mockito.when(ownerService.findAllByLastNameLike(Mockito.anyString()))
                .thenReturn(results);
        mockMvc.perform(MockMvcRequestBuilders.post("/owners/find")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", ownerLastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+id));
    }

    @Test
    void testFindOwnerMultiple() throws Exception {
        Mockito.when(ownerService.findAllByLastNameLike(Mockito.anyString()))
                .thenReturn(owners);
        mockMvc.perform(MockMvcRequestBuilders.post("/owners/find")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", ownerLastName))
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEW_OWNERS_INDEX))
                .andExpect(model().attribute("owners", hasSize(owners.size())));
    }

    @Test
    void testFindOwnerNoResult() throws Exception {
        Mockito.when(ownerService.findAllByLastNameLike(Mockito.anyString()))
                .thenReturn(new HashSet<>());
        mockMvc.perform(MockMvcRequestBuilders.post("/owners/find")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", ownerLastName))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/find"))
                .andExpect(model().attributeHasFieldErrors("owner","lastName"))
                .andExpect(model().attribute("owner",hasProperty("lastName", is(ownerLastName))));
    }

    @Test
    void testCreateOwner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEW_OWNERS_FORM))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void testUpdateOwner() throws Exception {
        Mockito.when(ownerService.findById(Mockito.anyLong())).thenReturn(owners.stream().findFirst().orElse(null));
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/"+id+"/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEW_OWNERS_FORM))
                .andExpect(model().attribute("owner",hasProperty("id", is(id))));
    }

    @Test
    void testPerformCreate() throws Exception{
        Mockito.when(ownerService.save(Mockito.any(Owner.class)))
                .then(parameter -> {
                    //Set owner ID
                    Owner owner = parameter.getArgument(0);
                    owner.setId(id);
                    return owner;
                });
        mockMvc.perform(MockMvcRequestBuilders.post("/owners/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+id));
    }

    @Test
    void testPerformUpdate() throws Exception{
        Mockito.when(ownerService.save(Mockito.any(Owner.class)))
                .then(parameter -> {
                    //Set owner ID
                    Owner owner = parameter.getArgument(0);
                    owner.setId(id);
                    return owner;
                });
        mockMvc.perform(MockMvcRequestBuilders.post("/owners/"+id+"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+id));
    }
}
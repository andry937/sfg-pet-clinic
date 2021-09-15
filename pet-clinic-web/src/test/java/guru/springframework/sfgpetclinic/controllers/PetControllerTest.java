package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetControllerTest {
    private static final Long PET_ID = 1L;
    PetController petController;
    @Mock
    PetTypeService petTypeService;
    @Mock
    OwnerService ownerService;
    @Mock
    PetService petService;

    MockMvc mockMvc;

    private static final Long OWNER_ID= 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        petController = new PetController(petTypeService, ownerService, petService);
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        Mockito.when(ownerService.findById(Mockito.anyLong()))
                .thenReturn(Owner.builder().id(OWNER_ID).build());
    }

    @Test
    public void testCreatePet()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/owners/%d/pets/new", OWNER_ID)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name(PetController.VIEW_PETS_FORM));
    }

    @Test
    public void testPerformCreate() throws Exception{
        Mockito.when(petService.save(Mockito.any(Pet.class)))
                .thenReturn(Pet.builder().owner(Owner.builder().id(OWNER_ID).build()).build());
        mockMvc.perform(MockMvcRequestBuilders.post(String.format("/owners/%d/pets/new", OWNER_ID))
                        .param("name", "Dogy")
                        .param("birthDate","2003-08-09")
                        .param("petType", "1"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+OWNER_ID));
    }

    @Test
    public void testUpdatePet()throws Exception{
        Mockito.when(petService.findById(Mockito.anyLong()))
                .thenReturn(Pet.builder().id(PET_ID).build());
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/owners/%d/pets/%d/edit", OWNER_ID, PET_ID)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pet", hasProperty("id", is(PET_ID))))
                .andExpect(view().name(PetController.VIEW_PETS_FORM));
    }

    @Test
    public void testPerformUpdate() throws Exception{
        Mockito.when(petService.save(Mockito.any(Pet.class)))
                .thenReturn(Pet.builder().owner(Owner.builder().id(OWNER_ID).build()).build());
        mockMvc.perform(MockMvcRequestBuilders.post(String.format("/owners/%d/pets/%d/edit", OWNER_ID, PET_ID))
                        .param("name", "Dogy")
                        .param("birthDate","2003-08-09")
                        .param("petType", "1"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/"+OWNER_ID));
    }

}
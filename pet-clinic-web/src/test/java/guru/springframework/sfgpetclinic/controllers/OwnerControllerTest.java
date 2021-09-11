package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OwnerControllerTest {
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
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController)
                .build();
        Mockito.when(ownerService.findAll()).thenReturn(owners);
        Mockito.when(ownerService.findById(Mockito.anyLong())).thenReturn(owners.stream().findFirst().orElse(new Owner()));
    }
    @Test
    void listOwners() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owners"))
                .andExpect(view().name("owners/index"));
    }

    @Test
    void showOwners() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/"+id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/details"));
    }
}